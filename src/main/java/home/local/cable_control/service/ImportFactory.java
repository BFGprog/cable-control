package home.local.cable_control.service;

import home.local.cable_control.mapper.CableMapper;
import home.local.cable_control.mapper.WarehouseMapper;
import home.local.cable_control.repository.CableRepository;
import home.local.cable_control.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImportFactory {

    private final CableService excelImportService;
    private final CableMapper cableMapper;
    private final WarehouseMapper warehouseMapper;
    private final CableRepository cableRepository;
    private final WarehouseRepository warehouseRepository;

    public void importCables(InputStream is) {
        excelImportService.importFromExcel(is, cableMapper, cableRepository);
    }

    public void importWarehouses(InputStream is) {
        //excelImportService.importFromExcel(is, warehouseMapper, warehouseRepository);
    }


}
