package home.local.cable_control.mapper;

import home.local.cable_control.model.Cable;
import home.local.cable_control.model.dto.CableDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CableMapper implements ExcelRowMapper<Cable>  {

    private final DataFormatter formatter = new DataFormatter();

    @Override
    public Cable fromRow(Row row) {
        Cable cable = new Cable();
        if(getString(row.getCell(7)) == null || Objects.equals(getString(row.getCell(7)), "")){
            return null;
        }

        cable.setCreatedDate(LocalDateTime.now());
        cable.setRequest(getString(row.getCell(1)));
        cable.setStatusCable(getString(row.getCell(2)));
        cable.setTightenDate(getStringDate(row.getCell(3)));
        cable.setTightenPerson(getString(row.getCell(4)));
        cable.setQueue(getString(row.getCell(6)));
        cable.setIndex(getString(row.getCell(7)));
        cable.setMark(getString(row.getCell(8)));
        cable.setDesignLength(getDouble(row.getCell(9)));
        cable.setMeasuredLength(getMeasuredLength(row.getCell(10)));
        cable.setLimitLength(getDouble(row.getCell(12)));
        cable.setTinnedCopperBraid(getString(row.getCell(13)));
        cable.setTinnedCopperBraidAdded(getString(row.getCell(14)));
        cable.setNote(getString(row.getCell(15)));
        cable.setDeviceIn(getString(row.getCell(17)));
        cable.setRoomIn(getString(row.getCell(18)));
        cable.setRoomNameIn(getString(row.getCell(19)));
        cable.setRoomNameOut(getString(row.getCell(20)));
        cable.setRoomOut(getString(row.getCell(21)));
        cable.setDeviceOut(getString(row.getCell(22)));
        cable.setTypeMOrMe(getString(row.getCell(23)));
        cable.setRoute(getString(row.getCell(24)));
        cable.setElectricalSchematic(getString(row.getCell(34)));
        cable.setComplete(getBoolean(row.getCell(48)));
        cable.setIsolatedRouting(getBoolean(row.getCell(49)));
        cable.setStatus(getBoolean(row.getCell(50)));

        return cable;
    }

    private String getString(Cell cell) {
        return cell == null ? null : formatter.formatCellValue(cell).trim();
    }

    private double getDouble(Cell cell) {
        if (cell == null) return 0.0;

        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? cell.getNumericCellValue()
                    : Double.parseDouble(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private Double getMeasuredLength(Cell cell) {
        if (cell == null) return null;

        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? cell.getNumericCellValue()
                    : Double.parseDouble(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return null;
        }
    }

    private boolean getBoolean(Cell cell) {
        if (cell == null) return false;
        String value = formatter.formatCellValue(cell).toLowerCase();
        return value.equals("true") || value.equals("1") || value.equals("yes") || value.equals("да");
    }
    private LocalDate getStringDate(Cell cell) {
        if (cell == null) return null;
        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                return cell.getLocalDateTimeCellValue().toLocalDate();
            }
            if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue();
                if (value == null || value.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(
                        value.trim(),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy")
                );
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private LocalDate getDate(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;

        return cell.getDateCellValue()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private LocalDateTime getDateTime(Cell cell) {
        if (cell == null || cell.getCellType() != CellType.NUMERIC) return null;

        return cell.getDateCellValue()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }


    public CableDto toDto(Cable c) {
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

        return dto;
    }
    private String checkProperties(Cable c) {
        List<String> props = new ArrayList<>();
        if (c.isStatus()) props.add("Аннулирован");
        if (c.isIsolatedRouting()) props.add("Отд. от всех");
        if (c.isComplete()) props.add("Компл.");
        return props.isEmpty() ? null : String.join("\n", props);
    }
}