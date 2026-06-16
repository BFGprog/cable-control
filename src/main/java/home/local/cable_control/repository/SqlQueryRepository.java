package home.local.cable_control.repository;

import home.local.cable_control.model.SqlQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SqlQueryRepository extends JpaRepository<SqlQuery, Long> {
    List<SqlQuery> findAllByOrderByNumAscIdAsc();


}
