package home.local.cable_control.service;

import home.local.cable_control.model.documentdto.CableRow;
import home.local.cable_control.model.documentdto.CableRowTemp;
import home.local.cable_control.model.dto.QueryResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CableJournalParserService {

    //  public void cableJournalExport(InputStream inputStream) {
    private final DynamicExcelExportService dynamicExcelExportService;

    public ResponseEntity<InputStreamResource> parseAndExport(InputStream inputStream, String fileName) throws IOException {
        List<List<String>> rowsWord = readDocument(inputStream, fileName);
        //List<CableRow> rowsList = buildRows(rowsWord);
        List<List<Object>> rows = buildRows(rowsWord);//toQueryResult(rowsList);
        List<String> columns = getColumns();

        String nameDoc = fileName;
        if (nameDoc.contains(".")) {
            nameDoc = nameDoc.substring(0, nameDoc.lastIndexOf('.'));
        }

        QueryResult value = new QueryResult(columns, rows);
        return dynamicExcelExportService.createdDoc(value, nameDoc);
    }

    private List<List<Object>> buildRows(List<List<String>> rows) {
        CableRow cableRow = new CableRow();
        CableRowTemp cableRowTemp = new CableRowTemp();
        boolean flag = true;
        boolean flagTemp = false;
        int countRowTemp = 0;
        List<CableRow> cableRowList = new ArrayList<>();
        String electricalSchematic = null;
        List<List<Object>> rowsObject = new ArrayList<>();

        for (List<String> row : rows) {

            if (row.size() > 2) {
                if (row.get(2).startsWith("16450")) {
                    electricalSchematic = row.get(2).trim().split("\\s+")[0];
                    ;
                }
            }
            if (row.size() < 11) {
                continue;
            }
            try {
                if (Integer.parseInt(row.get(1).trim()) > 0 && !row.get(2).trim().equals("2")) {

                    if (!row.get(2).isBlank()
                            && row.size() >= 18
                            && !row.get(10).isBlank()
                            && !row.get(11).isBlank()
                            && !flag) {
                        mergeTemporaryData(cableRow, cableRowTemp);

                        rowsObject.add(toObject(cableRow));
                        //cableRowList.add(cableRow);

                        cableRow = new CableRow();
                        cableRowTemp = new CableRowTemp();
                        flag = true;
                        flagTemp = false;
                        countRowTemp = 0;
                    }
                    if (flagTemp) {
                        if (row.size() > 18) {
                            cableRowTemp.getType().add((row.get(2).trim()));
                            cableRowTemp.getMark().add((row.get(3).trim()));
                            if (!row.get(18).trim().equals("УЧТЕН")) {
                                cableRowTemp.getTinnedCopperBraid().add(row.get(18).trim());
                            }
                            countRowTemp++;
                        } else {
                            cableRowTemp.getType().add((row.get(2).trim()));
                            cableRowTemp.getMark().add((row.get(3).trim()));
                            if (countRowTemp > 0) {

                                String roomNameIn = cableRowTemp.getRoomNameIn();
                                String roomNameOut = cableRowTemp.getRoomNameOut();

                                if (roomNameIn == null || roomNameIn.isBlank()) {
                                    cableRowTemp.setRoomNameIn(row.get(6).trim());
                                } else {
                                    cableRowTemp.setRoomNameIn(cableRowTemp.getRoomNameIn() + row.get(6).trim());
                                }

                                if (roomNameOut == null || roomNameOut.isBlank()) {
                                    cableRowTemp.setRoomNameOut(row.get(7).trim());
                                } else {
                                    cableRowTemp.setRoomNameOut(cableRowTemp.getRoomNameOut() + row.get(7).trim());
                                }

                            }
                        }
                    }
                    if (flag) {
                        if (row.get(2).isBlank()) {
                            continue;
                        }
                        cableRow.setIndex(row.get(2).trim());
                        cableRow.setMark(row.get(3).trim());
                        cableRow.setRoomLength(getDouble(row.get(4).trim()));
                        cableRow.setDesignLength(getDouble(row.get(5).trim()));
                        cableRow.setDeviceIn(row.get(9).trim());
                        cableRow.setRoomIn(row.get(10).trim());
                        cableRow.setRoomOut(row.get(11).trim());
                        cableRow.setDeviceOut(row.get(12).trim());
                        if (row.size() > 18) {
                            cableRow.setTinnedCopperBraid(row.get(18).trim());
                        }
                        String index = cableRow.getIndex();
                        if (index != null
                                && !index.isEmpty()
                                && (index.charAt(index.length() - 1) == 'M'
                                || index.charAt(index.length() - 1) == 'М')) {

                            cableRow.setTypeMOrMe("М");
                            cableRow.setIndex(index.substring(0, index.length() - 1));
                        } else {
                            cableRow.setTypeMOrMe("МЕ");
                        }
                        cableRow.setElectricalSchematic(electricalSchematic);

                        flag = false;
                        flagTemp = true;
                    }
                }

            } catch (NumberFormatException ignored) {

            }
        }
        return rowsObject;
    }

    private void mergeTemporaryData(CableRow cableRow, CableRowTemp cableRowsTemp) {
        cableRow.setRoomNameOut(cableRowsTemp.getRoomNameOut());
        cableRow.setRoomNameIn(cableRowsTemp.getRoomNameIn());

        for (int i = 0; i < cableRowsTemp.getMark().size(); i++) {
            String mark = cableRowsTemp.getMark().get(i);
            if (mark != null && !mark.isEmpty() && Character.isDigit(mark.charAt(0))) {
                cableRow.setMark(cableRow.getMark() + " " + mark);
            } else {
                cableRow.setMark(cableRow.getMark() + cableRowsTemp.getMark().get(i));
            }
        }

        for (int i = 0; i < cableRowsTemp.getType().size(); i++) {
            String type = cableRowsTemp.getType().get(i);
            if (normalizeCyrillic(type).startsWith("ОТД.ОТ ВСЕХ")) {
                cableRow.setIsolatedRouting("ОТД.ОТ ВСЕХ");
            } else if (normalizeCyrillic(type).startsWith("ДЛИНА")) {
                cableRow.setLimitLength(
                        Double.parseDouble(
                                type.replaceAll("[^0-9.]", "")));
            } else if (normalizeCyrillic(type).startsWith("КОМПЛЕКТНО")) {
                cableRow.setComplete("КОМПЛЕКТНО");
            } else if (normalizeCyrillic(type).startsWith("ПМЛ ДВОЙНАЯ")) {
                cableRow.setDoubleCopperBraid("ПМЛ ДВОЙНАЯ");
            } else if (!type.isBlank()) {
                String note = cableRow.getNote();
                if (note == null || note.isBlank()) {
                    cableRow.setNote(type);
                } else {
                    cableRow.setNote(note + "\n" + type);
                }
            }
        }

        for (int i = 0; i < cableRowsTemp.getTinnedCopperBraid().size(); i++) {
            cableRow.setTinnedCopperBraid((cableRow.getTinnedCopperBraid() + " " + cableRowsTemp.getTinnedCopperBraid().get(i)).trim());
        }
    }

    public List<List<String>> readDocument(InputStream inputStream, String fileName) throws IOException {
        InputStream checkedInputStream = FileMagic.prepareToCheckMagic(inputStream);
        FileMagic fileMagic = FileMagic.valueOf(checkedInputStream);

        switch (fileMagic) {
            case OOXML:
                log.info("readDocx");
                return readDocx(checkedInputStream);
            case OLE2:
                log.info("readDoc");
                return readDoc(checkedInputStream);
            default:
                throw new IllegalArgumentException(
                        "Файл не является корректным Word документом: " + fileName
                );
        }

        /*
        String name = fileName.toLowerCase();
        if (name.endsWith(".docx")) {
            return readDocx(inputStream);
        }
        if (name.endsWith(".doc")) {
            return readDoc(inputStream);
        }
        throw new IllegalArgumentException("Поддерживаются только .doc и .docx");*/
    }

    private List<List<String>> readDocx(InputStream inputStream) throws IOException {
        List<List<String>> result = new ArrayList<>();
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                if (text == null || text.isBlank()) {
                    continue;
                }
                result.add(split(text));
            }
        }
        return result;
    }

    private List<List<String>> readDoc(InputStream inputStream) throws IOException {
        List<List<String>> result = new ArrayList<>();
        try (HWPFDocument document = new HWPFDocument(inputStream)) {
            Range range = document.getRange();
            for (int i = 0; i < range.numParagraphs(); i++) {
                String text = range.getParagraph(i).text();
                if (text == null) {
                    continue;
                }
                text = text
                        .replace("\r", "")
                        .replace("\u0007", "")
                        .trim();
                if (text.isBlank()) {
                    continue;
                }
                result.add(split(text));
            }
        }
        return result;
    }

    private List<String> split(String text) {
        List<String> row = new ArrayList<>();
        for (String value : text.split("\\|")) {
            row.add(value.trim());
        }
        return row;
    }

    private Double getDouble(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return Double.parseDouble(value.trim());
    }

    public static String normalizeCyrillic(String value) {
        if (value == null) {
            return null;
        }
        return value
                .replace('A', 'А')
                .replace('B', 'В')
                .replace('C', 'С')
                .replace('E', 'Е')
                .replace('H', 'Н')
                .replace('K', 'К')
                .replace('M', 'М')
                .replace('O', 'О')
                .replace('P', 'Р')
                .replace('T', 'Т')
                .replace('X', 'Х')
                .replace('Y', 'У');
    }

    private List<List<Object>> toQueryResult(List<CableRow> cableRows) {

        //List<List<Object>> rows =
        return cableRows.stream()
                .map(cableRow -> {
                    List<Object> row = new ArrayList<>();

                    row.add(cableRow.getIndex());
                    row.add(cableRow.getMark());
                    row.add(cableRow.getRoomLength());
                    row.add(cableRow.getDesignLength());
                    row.add(cableRow.getLimitLength());
                    row.add(cableRow.getDeviceIn());
                    row.add(cableRow.getRoomIn());
                    row.add(cableRow.getRoomNameIn());
                    row.add(cableRow.getRoomNameOut());
                    row.add(cableRow.getRoomOut());
                    row.add(cableRow.getDeviceOut());
                    row.add(cableRow.getTypeMOrMe());
                    row.add(cableRow.getTinnedCopperBraid());
                    row.add(cableRow.getDoubleCopperBraid());
                    row.add(cableRow.getIsolatedRouting());
                    row.add(cableRow.getComplete());
                    row.add(cableRow.getNote());
                    row.add(cableRow.getElectricalSchematic());

                    return row;
                })
                .toList();
    }


    private List<Object> toObject(CableRow cableRow) {
        List<Object> row = new ArrayList<>();

        row.add(cableRow.getIndex());
        row.add(cableRow.getMark());
        row.add(cableRow.getRoomLength());
        row.add(cableRow.getDesignLength());
        row.add(cableRow.getLimitLength());
        row.add(cableRow.getDeviceIn());
        row.add(cableRow.getRoomIn());
        row.add(cableRow.getRoomNameIn());
        row.add(cableRow.getRoomNameOut());
        row.add(cableRow.getRoomOut());
        row.add(cableRow.getDeviceOut());
        row.add(cableRow.getTypeMOrMe());
        row.add(cableRow.getTinnedCopperBraid());
        row.add(cableRow.getDoubleCopperBraid());
        row.add(cableRow.getIsolatedRouting());
        row.add(cableRow.getComplete());
        row.add(cableRow.getNote());
        row.add(cableRow.getElectricalSchematic());
        row.add("нет");

        return row;
    }

    private List<String> getColumns() { //[1-ГС-115, СПпВЭнг-БГ 3Х2Х0.75, null, 0.4, null, 1-ГС236, 03401, Электростанция, Электростанция, 03401, 1-ГС235, МЕ, , null, null, null, null, 16450.362642.001Э4, нет]
        //[1-У-184, СПпВЭнг-БГ 2Х2Х0.75, 3.0, null, null, С.07(ГРУ 1), 03401, Электростанция, Аппаратная навигационного оборудов, 03902, R-U-3-AD (A35, М, ПМЛ 10Х16, ПМЛ ДВОЙНАЯ, null, null, null, 16450.362653.001Э4, нет]
        return List.of("Индекс"
                , "Марка"
                , "Длина в помещении"
                , "Длина проектная"
                , "Ограничение по длине"
                , "Прибор откуда"
                , "Номер помещения откуда"
                , "Наименование помещения откуда"
                , "Наименование помещения куда"
                , "Номер помещения куда"
                , "Прибор куда"
                , "Признак М/МЕ"
                , "Плетенка марка и размер"
                , "Двойная ПМЛ"
                , "Отдельная прокладка"
                , "Комплектно"
                , "Примечание"
                , "Схема"
                , "Аннулирован" );
    }

}
