package home.local.cable_control.service;

import home.local.cable_control.model.WarehouseReplacement;
import home.local.cable_control.repository.WarehouseReplaceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseReplacementService {

    private final WarehouseReplaceRepository warehouseReplaceRepository;
    private Map<String, String> dictionaryMap;


    public Map<String, String> init() {

        return warehouseReplaceRepository.findAll()
                .stream()
                .collect(Collectors.toMap(
                        WarehouseReplacement::getWrongMark,
                        WarehouseReplacement::getCorrectMark,
                        (a, b) -> a
                ));
    }

    public String getCorrectMark(String mark) {
        if (mark == null) return null;
        return dictionaryMap.getOrDefault(mark, mark);
    }
}

