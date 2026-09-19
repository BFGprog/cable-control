package home.local.cable_control.model;

import home.local.cable_control.model.enam.DocumentApprovalStage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CableJournal extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String journalNum;
    private String notice;
    private LocalDate noticeDate;
    private int actualStatus;
    private int status;
    private DocumentApprovalStage documentApprovalStage;
    @OneToMany(mappedBy = "cableJournal")
    private List<CableSchedule> cableSchedules;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ship_id", nullable = false)
    private Ship ship;

}
