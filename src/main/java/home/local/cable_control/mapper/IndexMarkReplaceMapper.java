package home.local.cable_control.mapper;

import home.local.cable_control.model.IndexMarkReplace;
import home.local.cable_control.model.export.IndexMarkReplFomPars;
import home.local.cable_control.repository.IndexMarkReplaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class IndexMarkReplaceMapper {


    private final DataFormatter formatter = new DataFormatter();
    private final IndexMarkReplaceRepository indexMarkReplaceRepository;

    public List<IndexMarkReplace> fromRow(Row row, Map<String, String> replaceMark) {

        IndexMarkReplFomPars indexMarkReplFomPars = new IndexMarkReplFomPars();

        indexMarkReplFomPars.setIndex(getString(row.getCell(1)));
        indexMarkReplFomPars.setMarkOld(getString(row.getCell(2)));
        indexMarkReplFomPars.setMarkReplace1(getString(row.getCell(3)));
        indexMarkReplFomPars.setMarkReplace2(getString(row.getCell(4)));
        indexMarkReplFomPars.setMarkReplace3(getString(row.getCell(5)));
        indexMarkReplFomPars.setMarkReplace4(getString(row.getCell(6)));
        indexMarkReplFomPars.setMarkReplace5(getString(row.getCell(7)));
        indexMarkReplFomPars.setDesignLength(getDouble(row.getCell(8)));
        indexMarkReplFomPars.setAgreedMark(getString(row.getCell(9)));
        indexMarkReplFomPars.setNum(getInteger(row.getCell(10)));
        indexMarkReplFomPars.setLetterOutNum(getString(row.getCell(11)));
        indexMarkReplFomPars.setLetterOutDate(getString(row.getCell(12)));
        indexMarkReplFomPars.setLetterInNum(getString(row.getCell(13)));
        indexMarkReplFomPars.setLetterInDate(getString(row.getCell(14)));
        indexMarkReplFomPars.setCancelMark(getString(row.getCell(15)));
        indexMarkReplFomPars.setNote(getString(row.getCell(16)));
        indexMarkReplFomPars.setShip(getString(row.getCell(17)));

        return pars(indexMarkReplFomPars, replaceMark);
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

    private int getInteger(Cell cell) {
        if (cell == null) return 0;

        try {
            return cell.getCellType() == CellType.NUMERIC
                    ? (int) cell.getNumericCellValue()
                    : Integer.parseInt(formatter.formatCellValue(cell));
        } catch (Exception e) {
            return 0;
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

    private List<IndexMarkReplace> pars(IndexMarkReplFomPars entity, Map<String, String> replaceMark) {
        List<IndexMarkReplace> result = new ArrayList<>();
        List<String> replacements = List.of(
                entity.getMarkReplace1(),
                entity.getMarkReplace2(),
                entity.getMarkReplace3(),
                entity.getMarkReplace4(),
                entity.getMarkReplace5()
        );
        List<String> addsMark = new ArrayList<>();
        Set<String> agreedMark = Arrays.stream(
                        Optional.ofNullable(entity.getAgreedMark())
                                .orElse("")
                                .split("\\|")
                )
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
        addsMark = indexMarkReplaceRepository.findMarkByIndexes(entity.getIndex());
        addsMark.addAll(Arrays.stream(
                                replaceMark
                                        .getOrDefault(entity.getIndex() + entity.getLetterOutNum() + entity.getLetterOutDate() + entity.getShip(), "")
                                        .split("\\|")
                        )
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toList()
        );

        for (String markReplace : replacements) {
            if (markReplace != null && !markReplace.isBlank()) {
                IndexMarkReplace indexMarkReplace = new IndexMarkReplace();
                setParam(entity, indexMarkReplace, markReplace, agreedMark);
                result.add(indexMarkReplace);
            }
        }

        if (agreedMark.size() > 0) {
            for (String splitValue : agreedMark) {
                if (!replacements.contains(splitValue) && !splitValue.equalsIgnoreCase("все") && !addsMark.contains(splitValue)) {
                    log.info("<-> {} {}", splitValue, addsMark);
                    //addsMark.add(markReplace);
                    IndexMarkReplace indexMarkReplace = new IndexMarkReplace();
                    setParam(entity, indexMarkReplace, splitValue, agreedMark);

                    log.info("->" + agreedMark.size() + " " + indexMarkReplace.getIndex() + " " + splitValue + " " + addsMark.toString());
                    indexMarkReplace.setMarkReplace(splitValue);
                    indexMarkReplace.setStatus(2);
                    result.add(indexMarkReplace);
                }
            }
        }
        if (entity.getCancelMark() != null) {
            changStatusAgreedMark(entity);
        }
        return result;
    }

    private int checkAgreeMark(String mark, Set<String> agreedMark) {

        return agreedMark.stream()
                .map(String::trim)
                .anyMatch(agreed ->
                        agreed.equalsIgnoreCase("все")
                                || agreed.equalsIgnoreCase(mark)
                )
                ? 1
                : 0;
    }

    private void setParam(IndexMarkReplFomPars entity, IndexMarkReplace indexMarkReplace, String markReplace, Set<String> splitValues) {

        indexMarkReplace.setCreatedDate(LocalDateTime.now());
        indexMarkReplace.setIndex(entity.getIndex());
        indexMarkReplace.setMarkOld(entity.getMarkOld());
        indexMarkReplace.setMarkReplace(markReplace);
        indexMarkReplace.setDesignLength(entity.getDesignLength());
        indexMarkReplace.setAgreedMark(entity.getAgreedMark());
        indexMarkReplace.setStatus(checkAgreeMark(markReplace, splitValues));
        indexMarkReplace.setNum(entity.getNum());
        indexMarkReplace.setLetterOutNum(entity.getLetterOutNum());
        indexMarkReplace.setLetterOutDate(entity.getLetterOutDate());
        indexMarkReplace.setLetterInNum(entity.getLetterInNum());
        indexMarkReplace.setLetterInDate(entity.getLetterInDate());
        indexMarkReplace.setCancelMark(entity.getCancelMark());
        indexMarkReplace.setNote(entity.getNote());
        indexMarkReplace.setShip(entity.getShip());
    }

    private void changStatusAgreedMark(IndexMarkReplFomPars entity) {
        List<IndexMarkReplace> cancelMark = indexMarkReplaceRepository.findByIndexAndStatusGreaterThan(entity.getIndex(), 0);
        List<String> cancelMarks = Arrays.asList(entity.getCancelMark().split("\\|"));

        cancelMark.stream()
                .filter(c -> cancelMarks.contains(c.getMarkReplace()))
                .forEach(c -> {
                    c.setStatus(0);
                    c.setLetterInNum(c.getLetterInNum() + "|" + entity.getLetterInNum());
                    c.setLetterInDate(c.getLetterInDate() + "|" + entity.getLetterInDate());
                });
        indexMarkReplaceRepository.saveAll(cancelMark);
    }


}
