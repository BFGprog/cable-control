package home.local.cable_control.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CableChanged {
    /*Изменения кабеля*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private String statusCable;
    private LocalDateTime datRequest;
    private String tighten;
    private String queue;
    private String index;
    private String coreConductor;

}
