package home.local.cable_control.model.dto;

import home.local.cable_control.model.CableSchedule;
import home.local.cable_control.model.Ship;
import home.local.cable_control.model.enam.DocumentApprovalStage;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CableJournalDto {


    private Long id;
    private String journalNum;
    private String notice;
    private LocalDate noticeDate;
    private int actualStatus;
    private int status;
    private DocumentApprovalStage documentApprovalStage;
}
