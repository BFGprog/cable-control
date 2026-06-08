package home.local.cable_control.service;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import home.local.cable_control.mapper.CableMapper;
import home.local.cable_control.mapper.ExcelRowMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import home.local.cable_control.model.Cable;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import home.local.cable_control.repository.CableRepository;


@Service
@Slf4j
@RequiredArgsConstructor
public class CableService {
    private final CableMapper cableMapper;
    private final CableRepository cableRepository;

    private static final int CHECK_CABLE_SIZE = 500;

    @Transactional
    public <T> void importFromExcel(
            InputStream inputStream,
            ExcelRowMapper<T> mapper,
            JpaRepository<T, ?> repository) {

        List<T> batch = new ArrayList<>();
        int size = 0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                T entity = mapper.fromRow(row);
                if (entity != null) {
                    batch.add(entity);
                    size++;
                }

                if (batch.size() >= CHECK_CABLE_SIZE) {
                    repository.saveAll(batch);
                    batch.clear();
                }
            }
            if (!batch.isEmpty()) {
                log.info("saveAll: {}", size);
                repository.saveAll(batch);
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка импорта Excel", e);
        }
    }

    @Transactional
    public void importCableFromExcel(InputStream inputStream) {

        List<Cable> excelCables = new ArrayList<>();
        List<Cable> cables = new ArrayList<>();
        List<String> index = new ArrayList<>();
        List<Cable> saveCable = new ArrayList<>();
        int size = 0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Cable cable = cableMapper.fromRow(row);
                if (cable != null) {
                    excelCables.add(cable);
                    index.add(cable.getIndex());
                }
                if (excelCables.size() >= CHECK_CABLE_SIZE) {
                    cables = cableRepository.findByIndexes(index);
                    saveCable = checkChange(excelCables, cables);
                    cableRepository.saveAll(saveCable);
                    size += saveCable.size();
                    excelCables.clear();
                    index.clear();
                }
            }
            if (!excelCables.isEmpty()) {
                cables = cableRepository.findByIndexes(index);
                saveCable = checkChange(excelCables, cables);
                cableRepository.saveAll(saveCable);
                size += saveCable.size();
                log.info("saveAll: {}", size);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка импорта Excel", e);
        }
    }

    private List<Cable> checkChange(List<Cable> excelCables, List<Cable> dbCables) {
        Map<String, Cable> cablesMapByIndex = dbCables.stream()
                .collect(Collectors.toMap(
                        Cable::getIndex,
                        c -> c,
                        (a, b) -> a
                ));
        List<Cable> result = new ArrayList<>();
        for (Cable excelCable : excelCables) {
            Cable dbCable = cablesMapByIndex.get(excelCable.getIndex());
            if (dbCable == null) {
                result.add(excelCable);
            } else if (excelCable.hashCode() != dbCable.hashCode()) {
                if(!excelCable.equals(dbCable)){
                    excelCable.setId(dbCable.getId());
                    excelCable.setUpdateDate(LocalDateTime.now());
                    excelCable.setUpdateDate(dbCable.getCreatedDate());
                    result.add(excelCable);
                }
            }
        }
        return result;
    }


}
