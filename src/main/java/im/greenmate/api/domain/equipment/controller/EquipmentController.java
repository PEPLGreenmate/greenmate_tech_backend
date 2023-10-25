package im.greenmate.api.domain.equipment.controller;

import im.greenmate.api.domain.equipment.dto.SensorData;
import im.greenmate.api.domain.equipment.repository.EquipmentMemoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/equipments")
public class EquipmentController {

    private final EquipmentMemoryRepository equipmentMemoryRepository;

    @PostMapping
    public String sendInformation(@RequestBody SensorData request) {
        equipmentMemoryRepository.save(request);
        return request.toString();
    }

    @GetMapping
    public String viewInformation() {
        SensorData sensorData = equipmentMemoryRepository.findData();
        if (sensorData == null) {
            return "최근에 전송된 데이터가 없습니다.";
        }
        else {
            LocalDateTime lastUpdatedTime = equipmentMemoryRepository.findLastUpdatedTime();
            String parsedLocalDateTimeNow = lastUpdatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            String updatedTime = "최종 업데이트 = " + parsedLocalDateTimeNow + "\n";
            String dataString = sensorData.toString();
            return updatedTime + dataString;
        }
    }
}
