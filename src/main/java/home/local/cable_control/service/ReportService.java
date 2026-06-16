package home.local.cable_control.service;

import home.local.cable_control.mapper.ReportMapper;
import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.dto.Report;
import home.local.cable_control.repository.SqlQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {
    private final SqlQueryRepository sqlQueryRepository;
    private final ReportMapper reportMapper;

    public List<Report> getReports() {
        List<SqlQuery> listSqlQuery = sqlQueryRepository.findAllByOrderByNumAscIdAsc();
        return listSqlQuery.stream()
                .map(reportMapper::sqlQueryToReport)
                .toList();
    }


}
