package im.greenmate.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import im.greenmate.api.domain.equipment.dto.SensorData;
import im.greenmate.api.domain.equipment.repository.EquipmentMemoryRepository;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/plant")
public class DemoController {

    private final EquipmentMemoryRepository equipmentMemoryRepository;

    @GetMapping
    public DemoResponse getInformation() {
        SensorData data = equipmentMemoryRepository.findData();
        LocalDateTime lastUpdatedTime = equipmentMemoryRepository.findLastUpdatedTime();

        TemperatureState temperatureState = getTemperatureState(data.getTemp());
        HumidityState humidityState = getHumidityState(data.getHumi());
        LuminousState luminousState = getLuminousState(data.getLight());
        GroundHumidityState groundHumidityState = getGroundHumidityState(data.getGroundHumi());

        return DemoResponse.builder()
                .groundHumidity(data.getGroundHumi())
                .humidity(data.getHumi())
                .luminous(data.getLight())
                .temperature(data.getTemp())
                .temperatureState(temperatureState)
                .humidityState(humidityState)
                .luminousState(luminousState)
                .groundHumidityState(groundHumidityState)
                .lastUpdatedTime(lastUpdatedTime)
                .build();
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    @AllArgsConstructor
    static class DemoResponse {
        @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
        private LocalDateTime lastUpdatedTime;
        private int temperature;
        private int humidity;
        private int luminous;
        private int groundHumidity;
        private TemperatureState temperatureState;
        private HumidityState humidityState;
        private LuminousState luminousState;
        private GroundHumidityState groundHumidityState;
    }

    enum TemperatureState {
        COLD, NORMAL, HOT
    }

    enum HumidityState {
        DRY, NORMAL, WET
    }

    enum LuminousState {
        DARK, NORMAL, BRIGHT
    }

    enum GroundHumidityState {
        DRY, NORMAL, WET
    }


    private TemperatureState getTemperatureState(int temperature) {
        if (temperature < 21) return TemperatureState.COLD;
        else if (temperature < 25) return TemperatureState.NORMAL;
        else return TemperatureState.HOT;
    }

    private HumidityState getHumidityState(int humidity) {
        if (humidity < 40) return HumidityState.DRY;
        else if (humidity < 70) return HumidityState.NORMAL;
        else return HumidityState.WET;
    }

    private LuminousState getLuminousState(int luminous) {
        // 33% -> 800, 66% -> 1500
        if (luminous < 33) return LuminousState.DARK;
        else if (luminous < 66) return LuminousState.NORMAL;
        else return LuminousState.BRIGHT;
    }

    private GroundHumidityState getGroundHumidityState(int groundHumidityState) {
        if (groundHumidityState < 40) return GroundHumidityState.DRY;
        else if (groundHumidityState < 70) return GroundHumidityState.NORMAL;
        else return GroundHumidityState.WET;
    }
}
