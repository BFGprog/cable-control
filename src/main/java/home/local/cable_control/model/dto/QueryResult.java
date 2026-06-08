package home.local.cable_control.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult {

    private final List<String> columns;
    private final List<List<Object>> rows;
}
