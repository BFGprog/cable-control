package home.local.cable_control.service;

import home.local.cable_control.mapper.IndexMarkReplaceMapper;
import home.local.cable_control.model.IndexMarkReplace;
import home.local.cable_control.repository.IndexMarkReplaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class IndexMarkReplaceService {
    private final IndexMarkReplaceRepository indexMarkReplaceRepository;
    private final IndexMarkReplaceMapper indexMarkReplaceMapper;

    private static final int BATCH_SIZE = 500;

    @Transactional
    public void importIndexMarkReplaceService(InputStream inputStream) {

        List<IndexMarkReplace> result = new ArrayList<>();
        List<String> replacesMark = new ArrayList<>();
        Map<String, String> replacesMarkMap = new HashMap<>();
        int size = 0;

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                size++;
                if (row.getRowNum() == 0) continue;
                List<IndexMarkReplace> entity = indexMarkReplaceMapper.fromRow(row, replacesMarkMap);

                for (int i = 0; i < entity.size(); i++) {
                    String key = entity.get(i).getIndex() + entity.get(i).getLetterOutNum() + entity.get(i).getLetterOutDate()+ entity.get(i).getShip();
                    String newValue = entity.get(i).getMarkReplace();
                    String oldValue = replacesMarkMap.get(key);

                    if (oldValue != null && !oldValue.isBlank()) {
                        replacesMarkMap.put(key, oldValue + "|" + newValue);
                    } else {
                        replacesMarkMap.put(key, newValue);
                    }
                }

                assert entity != null;
                result.addAll(entity);

                if (result.size() >= BATCH_SIZE) {
                    indexMarkReplaceRepository.saveAll(result);
                    result.clear();
                }
            }
            if (!result.isEmpty()) {
                log.info("-> saveAll indexMarkReplace: {}", size);
                indexMarkReplaceRepository.saveAll(result);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
