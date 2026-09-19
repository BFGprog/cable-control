package home.local.cable_control.model.auxiliary;


import home.local.cable_control.model.enam.DocumentApprovalStage;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DocumentParameters {

    private Long documentId;
    private String documentNum;
    private String notice;
    private LocalDate noticeDate;
    private int actualStatus;
    private int documentType; //1 - all, 2 - part
    private DocumentApprovalStage documentApprovalStage; // BEFORE_APPROVAL, AFTER_APPROVAL
    private IncomingShip incomingShip;


}
