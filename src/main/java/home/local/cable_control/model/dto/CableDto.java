package home.local.cable_control.model.dto;


import lombok.Data;

import java.time.LocalDate;

@Data
public class CableDto {


    private Long id;
    private String request;
    private String statusCable;
    private LocalDate tightenDate;
    private String tightenPerson;
    private String queue;
    private String index;
    private String mark;
    private Double designLength;
    private Double measuredLength;
    private Double limitLength;
    private String tinnedCopperBraid;
    private String tinnedCopperBraidAdded;
    private String indexMarkRepl;
    private String properties;
    private String sourceDevice;
    private String sourceRoom;
    private String sourceRoomName;
    private String destinationRoomName;
    private String destinationRoom;
    private String destinationDevice;
    private String typeMOrMe;
    private String route;
    private String electricalSchematic;
    private String rooms;
    private String roomNames;
    private String devices;
    private String wareLength;
    private String wareNotes;


}
