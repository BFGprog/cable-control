package home.local.cable_control.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class SqlQuery {
    /*отчеты*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer num;
    private Integer type;
    @Column(columnDefinition = "TEXT")
    private String query;
    @OneToMany(mappedBy = "sqlQuery", cascade = CascadeType.ALL)
    @OrderBy("num ASC")
    private List<SqlQueryParam> params = new ArrayList<>();

}
