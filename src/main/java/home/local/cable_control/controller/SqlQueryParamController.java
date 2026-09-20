package home.local.cable_control.controller;

import home.local.cable_control.model.auxiliary.SqlQueryParamAdd;
import home.local.cable_control.service.SqlQueryParamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("/reports")
@Slf4j
@RequiredArgsConstructor
public class SqlQueryParamController {
    private final SqlQueryParamService sqlQueryParamService;

    @Value("${app.upload.zatichka1}")
    private String password1;
    @Value("${app.upload.zatichka2}")
    private String password2;


    @PostMapping("/{id}/param")
    public ResponseEntity<?> createdSqlQueryParam(@PathVariable Long id,
                                                  @RequestBody SqlQueryParamAdd sqlQueryAdd,
                                                  @RequestParam("code") String code) {
        if (!password1.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            return ResponseEntity.ok(sqlQueryParamService.created(id, sqlQueryAdd));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/param/{paramId}")
    public ResponseEntity<?> updateSqlQueryParam(@PathVariable Long id,
                                                 @PathVariable Long paramId,
                                                 @RequestBody SqlQueryParamAdd sqlQueryAdd,
                                                 @RequestParam("code") String code) {
        if (!password1.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            return ResponseEntity.ok(sqlQueryParamService.update(id, paramId, sqlQueryAdd));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/param/{paramId}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                                  @PathVariable Long paramId,
                                                  @RequestParam("code") String code) {
        if (!password1.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            sqlQueryParamService.replace(id, paramId);
            return ResponseEntity.ok("удален");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

}
