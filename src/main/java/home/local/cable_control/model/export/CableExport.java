package home.local.cable_control.model.export;

import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CableExport {

    String getWareLength();
    String getWareNotes();

    Long getId();
    Boolean getComplete();
    LocalDate getCreatedDate();
    LocalDateTime getDatRequest();
    Double getDesignLength();
    String getDeviceIn();
    String getDeviceOut();
    String getElectricalSchematic();
    String getIndex();
    Boolean getIsolatedRouting();
    Double getLimitLength();
    String getMark();
    Double getMeasuredLength();
    String getIndexMarkRepl();
    String getNote();
    String getQueue();
    String getRequest();
    String getRoomIn();
    String getRoomNameIn();
    String getRoomNameOut();
    String getRoomOut();
    String getRoute();
    Boolean getStatus();
    String getStatusCable();
    LocalDate getTightenDate();
    String getTightenPerson();
    String getTinnedCopperBraid();
    String getTinnedCopperBraidAdded();
    String getTypeMOrMe();
    String getIndexMarkReplace();
}