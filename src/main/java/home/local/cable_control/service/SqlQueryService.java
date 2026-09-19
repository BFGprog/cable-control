package home.local.cable_control.service;

import home.local.cable_control.mapper.ReportMapper;
import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.auxiliary.SqlQueryAdd;
import home.local.cable_control.model.dto.Report;
import home.local.cable_control.repository.SqlQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class SqlQueryService {

    private final SqlQueryRepository sqlQueryRepository;
    private final ReportMapper reportMapper;

    public List<Report> getSqlQueries() {
        List<SqlQuery> listSqlQuery = sqlQueryRepository.findAllByOrderByNumAscIdAsc();
        return listSqlQuery.stream()
                .map(reportMapper::sqlQueryToReport)
                .toList();
    }

    public SqlQuery findById(Long id) {
        return sqlQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден отчет " + id));

    }

    public SqlQuery created(SqlQueryAdd sqlQueryAdd) {
        SqlQuery sqlQuery = new SqlQuery();

        sqlQuery.setName(sqlQueryAdd.getName());
        sqlQuery.setNum(sqlQueryAdd.getNum());
        sqlQuery.setType(sqlQueryAdd.getType());
        sqlQuery.setQuery(sqlQueryAdd.getQuery());

        return sqlQueryRepository.save(sqlQuery);
    }

    public SqlQuery update(Long id, SqlQueryAdd sqlQueryAdd) {
        SqlQuery sqlQuery = sqlQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найден отчет " + id));
        sqlQuery.setName(sqlQueryAdd.getName());
        sqlQuery.setNum(sqlQueryAdd.getNum());
        sqlQuery.setType(sqlQueryAdd.getType());
        sqlQuery.setQuery(sqlQueryAdd.getQuery());

        return sqlQueryRepository.save(sqlQuery);
    }

    public void replace(Long id) {
        sqlQueryRepository.deleteById(id);
        log.info("Удален SqlQuery - " + id);
    }


}
