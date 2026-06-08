package home.local.cable_control.mapper;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelRowMapper<T> {
    T fromRow(Row row);
}
