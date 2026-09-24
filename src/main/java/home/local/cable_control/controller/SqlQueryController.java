package home.local.cable_control.controller;


import home.local.cable_control.model.auxiliary.SqlQueryAdd;
import home.local.cable_control.service.SqlQueryService;
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
public class SqlQueryController {

    private final SqlQueryService sqlQueryService;

    @Value("${app.upload.zatichka1}")
    private String password1;
    @Value("${app.upload.zatichka2}")
    private String password2;


    @GetMapping /*all sqlQuery*/
    public ResponseEntity<?> getReports(@RequestParam String code) {
        if (!this.password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        return ResponseEntity.ok(sqlQueryService.getSqlQueries());
    }

    @PostMapping
    public ResponseEntity<?> created(@RequestBody SqlQueryAdd sqlQueryAdd,
                                     @RequestParam("code") String code) {
        if (!password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            return ResponseEntity.ok(sqlQueryService.created(sqlQueryAdd));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody SqlQueryAdd sqlQueryAdd,
                                    @RequestParam("code") String code) {
        if (!password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            return ResponseEntity.ok(sqlQueryService.update(id, sqlQueryAdd));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @RequestParam("code") String code) {
        if (!password2.equals(code)) {
            return ResponseEntity.status(403).body("Wrong password");
        }
        try {
            sqlQueryService.replace(id);
            return ResponseEntity.ok("удален");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("ERROR: " + e.getMessage());
        }
    }




}
