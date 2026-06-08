package home.local.cable_control.service;

import home.local.cable_control.mapper.CableExportMapper;
import home.local.cable_control.mapper.CableMapper;
import home.local.cable_control.model.Cable;
import home.local.cable_control.model.dto.CableDto;
import home.local.cable_control.repository.CableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CableExportService {

    private final CableRepository cableRepository;
    private final CableMapper mapper;
    private final CableExportMapper cableExportMapper;

    public ByteArrayInputStream exportToExcel() {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("КЖ");
            List<CableDto> dtos = cableRepository.findAllCableExport()
                    .stream()
                    .map(cableExportMapper::toDto)
                    .toList();

            Font mainFont = workbook.createFont();
            mainFont.setFontName("Times New Roman");
            mainFont.setFontHeightInPoints((short) 12);

            Font headerFont = workbook.createFont();
            headerFont.setFontName("Times New Roman");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);

            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            headerStyle.setWrapText(true);

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.LEFT);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setWrapText(true);
            cellStyle.setFont(mainFont);

            DataFormat dataFormat = workbook.createDataFormat();
            CellStyle dateStyle = workbook.createCellStyle();
            dateStyle.cloneStyleFrom(cellStyle);
            dateStyle.setDataFormat(dataFormat.getFormat("dd.MM.yyyy"));

            CellStyle highlightStyle = workbook.createCellStyle();
            highlightStyle.cloneStyleFrom(cellStyle);
            highlightStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            highlightStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);


            Row head = sheet.createRow(0);
            String[] columns = {
                    "ID", "Заявка", "Статус", "Дата затяжки", "Затянул",
                    "Очередь", "Индекс", "Марка",
                    "Проектная длина", "Замер", "Ограничение длины", "Склад наличие","Склад бухты",
                    "Согласованые замены",
                    "Примечение",
                    "ПМЛ", "Доп. ПМЛ",
                    "Прибор откуда", "Помещение откуда", "Наименование помещения откуда",
                    "Наименование помещения куда", "Помещение куда", "Прибор куда",
                    "Признак М/МЕ", "Маршрут", "Схема",
                    "Помещения", "Наименование помещений", "Приборы"
            };

            for (int i = 0; i < columns.length; i++) {
                //head.createCell(i).setCellValue(columns[i]);
                Cell cell = head.createCell(i);
                cell.setCellValue(columns[i]);
                cell.setCellStyle(headerStyle);
            }

            sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, columns.length - 1));
            sheet.createFreezePane(0, 1);

            int rowIdx = 1;
            Row row;

            for (CableDto dto : dtos) {
                row = sheet.createRow(rowIdx++);
                int col = 0;

                col = createCell(row, col, dto.getId(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getRequest(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getStatusCable(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getTightenDate(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getTightenPerson(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getQueue(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getIndex(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getMark(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getDesignLength(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getMeasuredLength(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getLimitLength(), cellStyle, dateStyle, highlightStyle);
                col = createCell(row, col, dto.getWareLength(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getWareNotes(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getIndexMarkRepl(), cellStyle, dateStyle);//
                col = createCell(row, col, dto.getProperties(), cellStyle, dateStyle, highlightStyle);
                col = createCell(row, col, dto.getTinnedCopperBraid(), cellStyle, dateStyle, highlightStyle);
                col = createCell(row, col, dto.getTinnedCopperBraidAdded(), cellStyle, dateStyle, highlightStyle);
                col = createCell(row, col, dto.getSourceDevice(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getSourceRoom(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getSourceRoomName(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getDestinationRoomName(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getDestinationRoom(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getDestinationDevice(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getTypeMOrMe(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getRoute(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getElectricalSchematic(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getRooms(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getRoomNames(), cellStyle, dateStyle);
                col = createCell(row, col, dto.getDevices(), cellStyle, dateStyle);
            }
/*
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }*/
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                int maxWidth = 6500;
                int minWidth = 2500;
                if (columns[i].equals("Маршрут")) {
                    sheet.setColumnWidth(i, 11000);
                } else if (currentWidth > maxWidth) {
                    sheet.setColumnWidth(i, maxWidth);
                } else if (currentWidth < minWidth) {
                    sheet.setColumnWidth(i, minWidth);
                }
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            throw new RuntimeException("Ошибка экспорта Excel", e);
        }
    }

    private int createCell(Row row, int col, Object value, CellStyle style, CellStyle dateStyle) {
        Cell cell = row.createCell(col);

        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(style);

        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
            cell.setCellStyle(style);

        } else if (value instanceof LocalDate localDate) {
            cell.setCellValue(java.sql.Date.valueOf(localDate));
            cell.setCellStyle(dateStyle);

        } else if (value instanceof LocalDateTime localDateTime) {
            cell.setCellValue(java.sql.Timestamp.valueOf(localDateTime));
            cell.setCellStyle(dateStyle);

        } else if (value instanceof Date date) {
            cell.setCellValue(date);
            cell.setCellStyle(dateStyle);

        } else {
            cell.setCellValue(value.toString());
            cell.setCellStyle(style);
        }

        return col + 1;
    }

    private int createCell(Row row, int col, Object value,
                           CellStyle style, CellStyle dateStyle,
                           CellStyle highlightStyle) {

        Cell cell = row.createCell(col);

        if (value == null) {
            cell.setCellValue("");
            cell.setCellStyle(style);

        } else if (value instanceof Number number) {
            double num = number.doubleValue();
            cell.setCellValue(num);
            cell.setCellStyle(num != 0 ? highlightStyle : style);

        } else if (value instanceof LocalDate localDate) {
            cell.setCellValue(java.sql.Date.valueOf(localDate));
            cell.setCellStyle(style);

        } else if (value instanceof LocalDateTime localDateTime) {
            cell.setCellValue(java.sql.Timestamp.valueOf(localDateTime));
            cell.setCellStyle(style);

        } else if (value instanceof Date date) {
            cell.setCellValue(date);
            cell.setCellStyle(style);

        } else {
            String str = value.toString();
            cell.setCellValue(str);
            cell.setCellStyle(!str.isBlank() ? highlightStyle : style);
        }

        return col + 1;
    }

}