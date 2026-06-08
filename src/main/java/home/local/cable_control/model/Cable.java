package home.local.cable_control.model;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "idx_cable_index", columnList = "index"),
        @Index(name = "idx_cable_mark", columnList = "mark")
})
@Data
public class Cable {
    /*Кабель*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime  createdDate;
    private LocalDateTime  updateDate;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String request;
    private LocalDateTime datRequest;
    private String statusCable;
    private LocalDate tightenDate;
    private String tightenPerson;
    private String queue;
    @Column(unique = true)
    private String index;
    private String mark;
    private Double designLength;
    private Double measuredLength;
    private Double limitLength;
    private String tinnedCopperBraid;
    private String tinnedCopperBraidAdded;
    private String note;
    private String deviceIn;
    private String roomIn;
    private String roomNameIn;
    private String roomNameOut;
    private String roomOut;
    private String deviceOut;
    private String typeMOrMe;
    @Column(length = 1000)
    private String route;
    private String electricalSchematic;
    private boolean isolatedRouting;
    private boolean complete;
    private boolean status;
   /* private RequestCable requestCable;
    private Drawing sourceDrawing;
    private Drawing destinationDrawing;
    private ElectricalSchematic electricalSchematic;
    private User createdUser;
    private User changedUser;
    private LocalDate changedDate;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cable cable = (Cable) o;
        return isolatedRouting == cable.isolatedRouting && complete == cable.complete && status == cable.status && Objects.equals(request, cable.request) && Objects.equals(datRequest, cable.datRequest) && Objects.equals(statusCable, cable.statusCable) && Objects.equals(tightenDate, cable.tightenDate) && Objects.equals(tightenPerson, cable.tightenPerson) && Objects.equals(queue, cable.queue) && Objects.equals(index, cable.index) && Objects.equals(mark, cable.mark) && Objects.equals(designLength, cable.designLength) && Objects.equals(measuredLength, cable.measuredLength) && Objects.equals(limitLength, cable.limitLength) && Objects.equals(tinnedCopperBraid, cable.tinnedCopperBraid) && Objects.equals(tinnedCopperBraidAdded, cable.tinnedCopperBraidAdded) && Objects.equals(note, cable.note) && Objects.equals(deviceIn, cable.deviceIn) && Objects.equals(roomIn, cable.roomIn) && Objects.equals(roomNameIn, cable.roomNameIn) && Objects.equals(roomNameOut, cable.roomNameOut) && Objects.equals(roomOut, cable.roomOut) && Objects.equals(deviceOut, cable.deviceOut) && Objects.equals(typeMOrMe, cable.typeMOrMe) && Objects.equals(route, cable.route) && Objects.equals(electricalSchematic, cable.electricalSchematic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(request, datRequest, statusCable, tightenDate, tightenPerson, queue, index, mark, designLength, measuredLength, limitLength, tinnedCopperBraid, tinnedCopperBraidAdded, note, deviceIn, roomIn, roomNameIn, roomNameOut, roomOut, deviceOut, typeMOrMe, route, electricalSchematic, isolatedRouting, complete, status);
    }
}
