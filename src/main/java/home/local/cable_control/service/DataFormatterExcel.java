package home.local.cable_control.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DataFormatterExcel {

    private final DataFormatter formatter = new DataFormatter();

    public String getString(Cell cell) {
        return cell == null ? null : formatter.formatCellValue(cell).trim();
    }

    public double getDouble(Cell cell) {
        if (cell == null) return 0.0;

        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? cell.getNumericCellValue()
                    : Double.parseDouble(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return 0.0;
        }
    }


    public Double getMeasuredLength(Cell cell) {
        if (cell == null) return null;

        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? cell.getNumericCellValue()
                    : Double.parseDouble(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean getBoolean(Cell cell) {
        if (cell == null) return false;
        String value = formatter.formatCellValue(cell).toLowerCase();
        return value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("да");
    }

    public int getBooleanInt(Cell cell) {
        if (cell == null) return 0;
        String value = formatter.formatCellValue(cell).toLowerCase();
        if (value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("да")) {
            return 1;
        } else {
            return 0;
        }
    }

    public LocalDate getStringDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(
                        value.trim(),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                );
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
