package home.local.cable_control.mapper;

import home.local.cable_control.model.Warehouse;
import home.local.cable_control.service.WarehouseReplacementService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WarehouseMapper {

    private final WarehouseReplacementService warehouseReplacementService;
    private final DataFormatter formatter = new DataFormatter();

    public Warehouse fromRow(Row row, Map<String, String> dictionaryMap, Map<String, Integer> rowFields) {
        Warehouse w = new Warehouse();

        //Хранится Стеллаж №ЗАКАЗА Кол-во,  м.
        //w.setCreatedDate(LocalDate.from(LocalDate.now().atStartOfDay()));
        w.setCreatedDate(LocalDateTime.now());
        w.setNumber(getNum(row.getCell(0)));
        w.setNum1C(getString(row.getCell(1)));
        w.setMark(
                this.getCorrectMark(
                        getString(row.getCell(2)) + " " +
                                getString(row.getCell(3))
                        , dictionaryMap
                )
        );
        w.setName(getString(row.getCell(2)));
        w.setCoreSectional(getString(row.getCell(3)));
        w.setLength(getDouble(row.getCell(rowFields.getOrDefault("Кол-во,  м.", 6))));
        w.setNote(getString2(row.getCell(rowFields.getOrDefault("Бухты на барабане", 7))));

        w.setReel(getString(row.getCell(rowFields.getOrDefault("№ЗАКАЗА", 9) - 1)));
        w.setShip(getString(row.getCell(rowFields.getOrDefault("№ЗАКАЗА", 9))));
        w.setPlace(
                getString(row.getCell(rowFields.getOrDefault("Стеллаж", 35))) + " " +
                        getString(row.getCell(rowFields.getOrDefault("Хранится", 36)))
        );
        w.setStatus(1);

        return w;
    }

    private String getString2(Cell cell) {
        if (cell == null) return null;
        return formatter.formatCellValue(cell);
    }

    private String getString(Cell cell) {
        if (cell == null) return "";
        String value = formatter.formatCellValue(cell).trim();
        return value.isEmpty() ? "" : value;
    }

    private int getNum(Cell cell) {
        if (cell == null) return 0;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return (int) cell.getNumericCellValue();
            }
            return Integer.parseInt(cell.toString().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private double getDouble(Cell cell) {
        if (cell == null) return 0;

        return switch (cell.getCellType()) {
            case NUMERIC -> cell.getNumericCellValue();
            case STRING -> {
                try {
                    yield Double.parseDouble(cell.getStringCellValue().replace(",", "."));
                } catch (Exception e) {
                    yield 0;
                }
            }
            default -> 0;
        };
    }

    private LocalDate getLocalDate(Cell cell) {
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        }

        try {
            return LocalDate.parse(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return null;
        }
    }

    public String getCorrectMark(String mark) {
        return warehouseReplacementService.getCorrectMark(mark);
    }

    public String getCorrectMark(String mark, Map<String, String> dictionaryMap) {
        if (mark == null) return null;
        return dictionaryMap.getOrDefault(mark, mark);
    }

    public Map<String, Integer> getRowField(Row row) {
        Map<String, Integer> rowField = new HashMap<>();
        int i = 0;
        //Хранится Стеллаж
        while (row.getCell(i) != null) {
            rowField.put(getString(row.getCell(i)), i);
            i++;
        }

        return rowField;
    }

}
