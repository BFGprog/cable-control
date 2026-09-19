package home.local.cable_control.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class SqlQueryParam {
    /*параметры отчета*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private Integer type;
    private Integer num;
    @ManyToOne(optional = false)
    @JoinColumn(name = "sql_query_id", nullable = false)
    private SqlQuery sqlQuery;

}
