package home.local.cable_control.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class CoreConductorChanged {
    /*замена марки кабеля*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String index;
    private String coreConductor;
    private String drawingMark;
    private String proposedMark;
    private String selectedMark;
    private Integer designLength;
    private Integer num;

}
