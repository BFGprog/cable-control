package home.local.cable_control.mapper;

import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.SqlQueryParam;
import home.local.cable_control.model.dto.Report;
import home.local.cable_control.model.dto.ReportParams;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportMapper {
    public Report sqlQueryToReport(SqlQuery sqlQuery) {
        Report report = new Report();
        report.setId(sqlQuery.getId());
        report.setName(sqlQuery.getName());
        report.setType(sqlQuery.getType());
        report.setNum(sqlQuery.getNum());
        report.setQuery(sqlQuery.getQuery());

        List<ReportParams> params = sqlQuery.getParams().stream()
                .map(this::sqlQueryParamToReportParams)
                .toList();
        report.setParams(params);

        return report;
    }

    public ReportParams sqlQueryParamToReportParams(SqlQueryParam sqlQueryParam) {
        ReportParams reportParams = new ReportParams();
        reportParams.setId(sqlQueryParam.getId());
        reportParams.setCode(sqlQueryParam.getCode());
        reportParams.setName(sqlQueryParam.getName());
        reportParams.setType(sqlQueryParam.getType());
        reportParams.setNum(sqlQueryParam.getNum());

        return reportParams;
    }
}
