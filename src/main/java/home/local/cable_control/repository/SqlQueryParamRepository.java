package home.local.cable_control.repository;

import home.local.cable_control.model.SqlQueryParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SqlQueryParamRepository  extends JpaRepository<SqlQueryParam, Long> {
}
