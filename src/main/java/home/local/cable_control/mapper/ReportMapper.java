package home.local.cable_control.mapper;

import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.dto.Report;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {
    public Report sqlQueryToReport (SqlQuery sqlQuery){
        Report report = new Report();
        report.setId(sqlQuery.getId());
        report.setName(sqlQuery.getName());
        return report;
    }
}
