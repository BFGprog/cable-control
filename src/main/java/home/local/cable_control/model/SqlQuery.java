package home.local.cable_control.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class SqlQuery {
    /*отчеты*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer num;
    @Column(columnDefinition = "TEXT")
    private String query;

}
