package home.local.cable_control.service;

import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.dto.QueryResult;
import home.local.cable_control.repository.SqlQueryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicExcelExportService {

    private final SqlQueryRepository sqlQueryRepository;
    private final JdbcDynamicQueryService queryService;

    public ResponseEntity<InputStreamResource> export(Long queryId) {
        SqlQuery sqlQuery = sqlQueryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));
        QueryResult result = queryService.getAlias(sqlQuery.getQuery());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sqlQuery.getName());
            int columnCount = result.getColumns().size();
            int rowCount = result.getRows().size();

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setWrapText(true);

            Row header = sheet.createRow(0);

            for (int i = 0; i < columnCount; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(result.getColumns().get(i));
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = 1;
            for (List<Object> rowData : result.getRows()) {
                Row row = sheet.createRow(rowIdx++);
                for (int i = 0; i < columnCount; i++) {
                    Cell cell = row.createCell(i);
                    Object value = rowData.get(i);
                    if (value instanceof Number n) {
                        cell.setCellValue(n.doubleValue());
                    } else if (value instanceof Boolean b) {
                        cell.setCellValue(b);
                    } else {
                        cell.setCellValue(value != null ? value.toString() : "");
                    }
                    cell.setCellStyle(cellStyle);
                }
            }

            sheet.setAutoFilter(new CellRangeAddress(
                    0,
                    rowCount,
                    0,
                    columnCount - 1
            ));

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                int maxWidth = 6500;
                int minWidth = 2500;
                if (result.getColumns().get(i).equals("rout") || result.getColumns().get(i).equalsIgnoreCase("маршрут")) {
                    sheet.setColumnWidth(i, 11000);
                } else if (currentWidth > maxWidth) {
                    sheet.setColumnWidth(i, maxWidth);
                } else if (currentWidth < minWidth) {
                    sheet.setColumnWidth(i, minWidth);
                }
            }
            workbook.write(out);

            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(out.size())
                    .body(new InputStreamResource(in));

        } catch (Exception e) {
            throw new RuntimeException("Ошибка Excel", e);
        }
    }
}
