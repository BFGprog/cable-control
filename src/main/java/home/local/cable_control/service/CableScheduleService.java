package home.local.cable_control.service;

import home.local.cable_control.model.CableJournal;
import home.local.cable_control.model.CableSchedule;
import home.local.cable_control.model.auxiliary.DocumentParameters;
import home.local.cable_control.repository.CableScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CableScheduleService {
    private final CableScheduleRepository cableScheduleRepository;

    private final DataFormatterExcel dataFormatterExcel;
    private final CableJournalService cableJournalService;

    private static final int CHECK_CABLE_SIZE = 500;

    public void importCable(InputStream inputStream, String fileName, DocumentParameters parameters) throws IOException {

        List<CableSchedule> saveCable = new ArrayList<>(CHECK_CABLE_SIZE);
        int size = 0;

        CableJournal cableJournal = cableJournalService.saveCableJournal(parameters);

        if (parameters.getDocumentId() != null) {
            cableScheduleRepository.deleteByCableJournal(cableJournal);
        }

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                CableSchedule cable = fromRow(row);
                cable.setCableJournal(cableJournal);
                saveCable.add(cable);
                if (saveCable.size() >= CHECK_CABLE_SIZE) {
                    cableScheduleRepository.saveAll(saveCable);
                    size += saveCable.size();
                    saveCable.clear();
                }
            }
            if (!saveCable.isEmpty()) {
                cableScheduleRepository.saveAll(saveCable);
                size += saveCable.size();
                log.info("saveAll: {}", size);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка импорта Excel", e);
        }
    }


    private CableSchedule fromRow(Row row) {
        CableSchedule cable = new CableSchedule();

        cable.setIndex(dataFormatterExcel.getString(row.getCell(1)));
        cable.setMark(dataFormatterExcel.getString(row.getCell(2)));
        cable.setRoomLength(dataFormatterExcel.getMeasuredLength(row.getCell(3)));
        cable.setDesignLength(dataFormatterExcel.getMeasuredLength(row.getCell(4)));
        cable.setLimitLength(dataFormatterExcel.getMeasuredLength(row.getCell(5)));
        cable.setDeviceIn(dataFormatterExcel.getString(row.getCell(6)));
        cable.setRoomIn(dataFormatterExcel.getString(row.getCell(7)));
        cable.setRoomNameIn(dataFormatterExcel.getString(row.getCell(8)));
        cable.setRoomNameOut(dataFormatterExcel.getString(row.getCell(9)));
        cable.setRoomOut(dataFormatterExcel.getString(row.getCell(10)));
        cable.setDeviceOut(dataFormatterExcel.getString(row.getCell(11)));
        cable.setTypeMOrMe(dataFormatterExcel.getString(row.getCell(12)));
        cable.setTinnedCopperBraid(dataFormatterExcel.getString(row.getCell(13)));
        cable.setDoubleCopperBraid(dataFormatterExcel.getString(row.getCell(14)));
        cable.setIsolatedRouting(dataFormatterExcel.getString(row.getCell(15)));
        cable.setComplete(dataFormatterExcel.getString(row.getCell(16)));
        cable.setNote(dataFormatterExcel.getString(row.getCell(17)));
        cable.setElectricalSchematic(dataFormatterExcel.getString(row.getCell(18)));
        cable.setStatusCable(dataFormatterExcel.getBooleanInt(row.getCell(19)));

        return cable;
    }


    public void deleteByCableJournal(CableJournal cableJournal) {
        cableScheduleRepository.deleteByCableJournal(cableJournal);
    }


}
