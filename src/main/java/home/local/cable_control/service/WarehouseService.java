package home.local.cable_control.service;

import home.local.cable_control.mapper.WarehouseMapper;
import home.local.cable_control.model.Warehouse;
import home.local.cable_control.repository.WarehouseReplaceRepository;
import home.local.cable_control.repository.WarehouseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseMapper warehouseMapper;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseReplacementService warehouseReplacementService;

    public record WarehouseKey(Integer number, String num1C, String mark) {
    }

    private static final int CHECK_WAREHOUSE_SIZE = 500;

    @Transactional
    public void importWarehouseFromExcel(InputStream inputStream) throws IOException {

        List<Warehouse> excelWarehouses = new ArrayList<>();
        List<Warehouse> saveWarehouses = new ArrayList<>();
        List<Integer> paramNumbers = new ArrayList<>();
        List<String> paramNum1Cs = new ArrayList<>();
        List<String> paramMarks = new ArrayList<>();
        List<Long> warehouseIdDelete = new ArrayList<>();
        Map<String, Integer> rowFields = new HashMap<>();
        Map<String, String> dictionaryMap = warehouseReplacementService.init();
        int size = 0;

        //try (
        Workbook workbook = new XSSFWorkbook(inputStream);//) {
        //Sheet sheet = workbook.getSheetAt(0);
        Sheet sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 1);
        if (workbook.getSheetAt(workbook.getNumberOfSheets() - 1).getSheetName().equals("Лист1")) {
            sheet = workbook.getSheetAt(workbook.getNumberOfSheets() - 2);
        }
        log.info(sheet.getSheetName() + " " + workbook.getNumberOfSheets());
        for (Row row : sheet) {
            if (row.getRowNum() == 0) { //continue;
                rowFields = warehouseMapper.getRowField(row);
                continue;
            }
            Warehouse warehouse = warehouseMapper.fromRow(row, dictionaryMap, rowFields);
            if (warehouse != null) {
                excelWarehouses.add(warehouse);
                paramNumbers.add(warehouse.getNumber());
                paramNum1Cs.add(warehouse.getNum1C());
                paramMarks.add(warehouse.getMark());
            }
            if (excelWarehouses.size() >= CHECK_WAREHOUSE_SIZE) {
                saveWarehouses = saveWarehouse(excelWarehouses, paramNumbers, paramNum1Cs, paramMarks);
                saveWarehouses.forEach(w -> warehouseIdDelete.add(w.getId()));
                warehouseRepository.saveAll(saveWarehouses);

                size += saveWarehouses.size();

                excelWarehouses.clear();
                saveWarehouses.clear();
                paramNumbers.clear();
                paramNum1Cs.clear();
                paramMarks.clear();
            }
        }
        if (!excelWarehouses.isEmpty()) {
            saveWarehouses = saveWarehouse(excelWarehouses, paramNumbers, paramNum1Cs, paramMarks);
            saveWarehouses.forEach(w -> warehouseIdDelete.add(w.getId()));
            warehouseRepository.saveAll(saveWarehouses);

            if (!warehouseIdDelete.isEmpty()) {
                warehouseRepository.deactivateNotInIds(warehouseIdDelete);
            }

            size += saveWarehouses.size();
            log.info("saveAllWarehouses: {}", size);
        }
        /*} catch (Exception e) {
            throw new RuntimeException("Ошибка импорта Excel", e);
        }*/
    }

    private List<Warehouse> saveWarehouse(List<Warehouse> excelWarehouses,
                                          List<Integer> paramNumbers,
                                          List<String> paramNum1cs,
                                          List<String> paramMarks) {

        List<Warehouse> dbWarehouses = warehouseRepository.findWarehouseByParam(
                paramNumbers.toArray(new Integer[0]),
                paramNum1cs.toArray(new String[0]),
                paramMarks.toArray(new String[0])
        );
        //List<Warehouse> toSave = checkChange(excelWarehouses, dbWarehouses);
        return checkChange(excelWarehouses, dbWarehouses);

        /*if (!toSave.isEmpty()) {
            warehouseRepository.saveAll(toSave);
        }
        return toSave.size();*/
    }

    private List<Warehouse> checkChange(List<Warehouse> excelWarehouses, List<Warehouse> dbWarehouses) {
        Map<WarehouseKey, Warehouse> dbMap = dbWarehouses.stream()
                .collect(Collectors.toMap(
                        w -> new WarehouseKey(w.getNumber(), w.getNum1C(), w.getMark()),
                        w -> w,
                        (a, b) -> a
                ));
        List<Warehouse> result = new ArrayList<>();
        for (Warehouse excelWarehouse : excelWarehouses) {
            WarehouseKey key = new WarehouseKey(
                    excelWarehouse.getNumber(),
                    excelWarehouse.getNum1C(),
                    excelWarehouse.getMark()
            );
            Warehouse dbWarehouse = dbMap.get(key);
            if (dbWarehouse == null) {
                result.add(excelWarehouse);
            } else if (excelWarehouse.hashCode() != dbWarehouse.hashCode()) {
                if (!excelWarehouse.equals(dbWarehouse)) {
                    excelWarehouse.setId(dbWarehouse.getId());
                    excelWarehouse.setUpdateDate(LocalDateTime.now());
                    excelWarehouse.setCreatedDate(dbWarehouse.getCreatedDate());
                    result.add(excelWarehouse);
                }
            }
        }
        return result;
    }


}
