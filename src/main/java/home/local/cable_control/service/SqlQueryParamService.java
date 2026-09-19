package home.local.cable_control.service;

import home.local.cable_control.model.SqlQuery;
import home.local.cable_control.model.SqlQueryParam;
import home.local.cable_control.model.auxiliary.SqlQueryParamAdd;
import home.local.cable_control.repository.SqlQueryParamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SqlQueryParamService {

    private final SqlQueryParamRepository sqlQueryParamRepository;
    private final SqlQueryService sqlQueryService;


    public SqlQueryParam created(Long id, SqlQueryParamAdd sqlQueryParamAdd) {
        SqlQueryParam sqlQueryParam = new SqlQueryParam();
        SqlQuery sqlQuery = sqlQueryService.findById(id);

        sqlQueryParam.setCode(sqlQueryParamAdd.getCode());
        sqlQueryParam.setName(sqlQueryParamAdd.getName());
        sqlQueryParam.setType(sqlQueryParamAdd.getType());
        sqlQueryParam.setNum(sqlQueryParamAdd.getNum());
        sqlQueryParam.setSqlQuery(sqlQuery);

        return sqlQueryParamRepository.save(sqlQueryParam);
    }

    public SqlQueryParam update(Long id, Long paramId, SqlQueryParamAdd sqlQueryParamAdd) {
        SqlQuery sqlQuery = sqlQueryService.findById(id);
        SqlQueryParam sqlQueryParam = sqlQueryParamRepository.findById(paramId)
                .orElseThrow(() -> new RuntimeException("Не найден параметр: " + paramId));

        sqlQueryParam.setCode(sqlQueryParamAdd.getCode());
        sqlQueryParam.setName(sqlQueryParamAdd.getName());
        sqlQueryParam.setType(sqlQueryParamAdd.getType());
        sqlQueryParam.setNum(sqlQueryParamAdd.getNum());

        return sqlQueryParamRepository.save(sqlQueryParam);
    }

    public void replace(Long id, Long paramId) {
        SqlQuery sqlQuery = sqlQueryService.findById(id);
        SqlQueryParam sqlQueryParam = sqlQueryParamRepository.findById(paramId)
                .orElseThrow(() -> new RuntimeException("Не найден параметр: " + paramId));
        sqlQueryParamRepository.deleteById(id);
        log.info("Удален sqlQueryParam: " + id);
    }


}

