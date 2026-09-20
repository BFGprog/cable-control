package home.local.cable_control.repository;

import home.local.cable_control.model.SqlQueryParam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SqlQueryParamRepository  extends JpaRepository<SqlQueryParam, Long> {

    Optional<SqlQueryParam> findByIdAndSqlQueryId(
            Long paramId,
            Long sqlQueryId
    );

}
