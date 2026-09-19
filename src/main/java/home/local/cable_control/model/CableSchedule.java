package home.local.cable_control.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class CableSchedule extends Auditable {
    /*Кабельный журнал. маршруты*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    private int statusCable;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cable_journal_id", nullable = false)
    private CableJournal cableJournal;

}
