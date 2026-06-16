package home.local.cable_control.mapper;

import home.local.cable_control.model.dto.CableDto;
import home.local.cable_control.model.export.CableExport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CableExportMapper {


    public CableDto toDto(CableExport c) {
        if (c == null) return null;

        CableDto dto = new CableDto();

        dto.setId(c.getId());
        dto.setRequest(c.getRequest());
        dto.setStatusCable(c.getStatusCable());
        dto.setTightenDate(c.getTightenDate());
        dto.setTightenPerson(c.getTightenPerson());
        dto.setQueue(c.getQueue());
        dto.setIndex(c.getIndex());
        dto.setMark(c.getMark());
        dto.setDesignLength(c.getDesignLength());
        dto.setMeasuredLength(c.getMeasuredLength());
        dto.setWareLength(c.getWareLength());
        dto.setWareNotes(c.getWareNotes());
        dto.setIndexMarkRepl(c.getIndexMarkRepl());
        dto.setProperties(checkProperties(c));
        dto.setLimitLength(c.getLimitLength());
        dto.setTinnedCopperBraid(c.getTinnedCopperBraid());
        dto.setTinnedCopperBraidAdded(c.getTinnedCopperBraidAdded());
        dto.setSourceDevice(c.getDeviceIn());
        dto.setSourceRoom(c.getRoomIn());
        dto.setSourceRoomName(c.getRoomNameIn());
        dto.setDestinationRoomName(c.getRoomNameOut());
        dto.setDestinationRoom(c.getRoomOut());
        dto.setDestinationDevice(c.getDeviceOut());
        dto.setTypeMOrMe(c.getTypeMOrMe());
        dto.setRoute(c.getRoute());
        dto.setElectricalSchematic(c.getElectricalSchematic());
        dto.setRooms(c.getRoomIn() + " - " + c.getRoomOut());
        dto.setRoomNames(c.getRoomNameIn() + " - " + c.getRoomNameOut());
        dto.setDevices(c.getDeviceIn() + " - " + c.getDeviceOut());
        dto.setIndexMarkReplace(c.getIndexMarkReplace());

        return dto;
    }
    private String checkProperties(CableExport c) {
        List<String> props = new ArrayList<>();
        if (c.getStatus()) props.add("Аннулирован");
        if (c.getIsolatedRouting()) props.add("Отд. от всех");
        if (c.getComplete()) props.add("Компл.");
        return props.isEmpty() ? null : String.join("\n", props);
    }
}
