package home.local.cable_control.model.documentdto;

import lombok.Data;

@Data
public class CableRow {

    private String index;
    private String mark;
    private Double roomLength;
    private Double designLength;
    private Double limitLength;
    private String deviceIn;
    private String roomIn;
    private String roomNameIn;
    private String roomNameOut;
    private String roomOut;
    private String deviceOut;
    private String typeMOrMe;
    private String tinnedCopperBraid;
    private String doubleCopperBraid;
    private String isolatedRouting;
    private String complete;
    private String note;
    private String electricalSchematic;


}
