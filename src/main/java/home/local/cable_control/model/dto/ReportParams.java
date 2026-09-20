package home.local.cable_control.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ReportParams {

    private Long id;
    private String code;
    private String name;
    private Integer num;
    private Integer type;
    private String query;
}
