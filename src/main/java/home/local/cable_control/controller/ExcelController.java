package home.local.cable_control.controller;

import home.local.cable_control.model.dto.Report;
import home.local.cable_control.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@CrossOrigin(value = "*")
//@RequestMapping("/excel")
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class ExcelController {

    private final CableService cableService;
    private final CableExportService cableExportService;
    private final DynamicExcelExportService dynamicExcelExportService;
    private final ReportService reportService;
    private final WarehouseService warehouseService;
    private final IndexMarkReplaceService indexMarkReplaceService;
    @Value("${app.upload.zatichka1}")
    private String password1;
    @Value("${app.upload.zatichka2}")
    private String password2;

    @PostMapping("/upload")
    public String uploadCable(@RequestParam("file") MultipartFile file,
                              @RequestParam("code") String code) {

        if (!password1.equals(code)) {
            return "ERROR: wrong password";
        }
        try {
            cableService.importCableFromExcel(file.getInputStream());
            return "OK";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @PostMapping("/upload1")
    public String uploadWarehouse(@RequestParam("file") MultipartFile file,
                                  @RequestParam("code") String code) {

        if (!password1.equals(code)) {
            return "ERROR: wrong password";
        }
        try {
            warehouseService.importWarehouseFromExcel(file.getInputStream());
            return "OK";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
    @PostMapping("/upload2")
    public String uploadIndexMarkReplace(@RequestParam("file") MultipartFile file,
                                  @RequestParam("code") String code) {

        if (!password1.equals(code)) {
            return "ERROR: wrong password";
        }
        try {
            indexMarkReplaceService.importIndexMarkReplaceService(file.getInputStream());
            return "OK";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }


    @GetMapping("/download") /*KJ*/
    public ResponseEntity<?> download(@RequestParam String code) {

        if (!this.password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        log.info("download");
        ByteArrayInputStream in = cableExportService.exportToExcel();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(in));
    }


    @GetMapping("/dynamic/{id}")
    public ResponseEntity<?> download(@PathVariable Long id, @RequestParam String code) {

        if (!this.password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        //ByteArrayInputStream in = dynamicExcelExportService.export(id);
        return dynamicExcelExportService.export(id);
    }

    @GetMapping("/reports") /*all sqlQuery*/
    public ResponseEntity<?> getReports(@RequestParam String code) {
        if (!this.password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        return ResponseEntity.ok(reportService.getReports());
    }
}
