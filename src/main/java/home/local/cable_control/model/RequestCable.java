package home.local.cable_control.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class RequestCable {
    /*Заявка*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdDate;
    private String name;
    private User createdUser;
    private boolean statusDelivery;
    private Warehouse warehouse;

}
