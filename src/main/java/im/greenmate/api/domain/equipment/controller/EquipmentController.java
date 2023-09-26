package im.greenmate.api.domain.equipment.controller;

import im.greenmate.api.domain.equipment.dto.SensorDataRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("api/equipments")
public class EquipmentController {

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> sendInformation(@RequestBody SensorDataRequest request) {
        log.info("request: {}", request);
        String requestString = request.toString();
        return ResponseEntity.ok(requestString);
    }
}
