package home.local.cable_control.controller;

import home.local.cable_control.model.auxiliary.DocumentParameters;
import home.local.cable_control.model.auxiliary.ReportRequest;
import home.local.cable_control.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

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
    private final WarehouseService warehouseService;
    private final IndexMarkReplaceService indexMarkReplaceService;
    private final CableJournalParserService cableJournalParserService;
    private final CableScheduleService cableScheduleService;

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


    @PostMapping("/dynamic") /* /{id}" @PathVariable Long id*/
    public ResponseEntity<?> download(@RequestBody ReportRequest params, @RequestParam String code) {

        if (!this.password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        //ByteArrayInputStream in = dynamicExcelExportService.export(id);
        try {
            return dynamicExcelExportService.export(params);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }


    @PostMapping("/uploadCableWord")
    public ResponseEntity<?> uploadCableWordDocument(@RequestParam("file") MultipartFile file,
                                                     @RequestParam("code") String code) {

        if (!password1.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            return cableJournalParserService.parseAndExport(file.getInputStream(), file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    @PostMapping("/uploadCableDocument")
    public ResponseEntity<?> uploadPreparedCableDocument(@RequestParam("file") MultipartFile file,
                                                         @RequestParam("code") String code,
                                                         @RequestParam("parameters") DocumentParameters parameters) {

        if (!password1.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            cableScheduleService.importCable(file.getInputStream(), file.getOriginalFilename(), parameters);
            return ResponseEntity.ok("Файл " + file.getOriginalFilename() + " загружен");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }


}
