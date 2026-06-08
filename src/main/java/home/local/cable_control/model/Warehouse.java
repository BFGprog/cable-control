package home.local.cable_control.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(indexes = {
        @Index(name = "idx_warehouse_mark", columnList = "mark")
})
@Data
public class Warehouse {
    /*Склад*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private String num1C;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String mark;
    private String name;
    private String coreSectional;
    private double length;
    private String note;
    private String ship;
    private String reel;
    private String place;
    private Integer status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warehouse warehouse = (Warehouse) o;
        return Double.compare(warehouse.length, length) == 0 && Objects.equals(number, warehouse.number) && Objects.equals(num1C, warehouse.num1C) && Objects.equals(mark, warehouse.mark) && Objects.equals(name, warehouse.name) && Objects.equals(coreSectional, warehouse.coreSectional) && Objects.equals(note, warehouse.note) && Objects.equals(ship, warehouse.ship) && Objects.equals(reel, warehouse.reel) && Objects.equals(place, warehouse.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, num1C, mark, name, coreSectional, length, note, ship, reel, place);
    }



}

