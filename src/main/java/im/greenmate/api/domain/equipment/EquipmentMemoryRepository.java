package im.greenmate.api.domain.equipment;

import im.greenmate.api.domain.equipment.dto.SensorData;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class EquipmentMemoryRepository {

    private SensorData sensorData;
    private LocalDateTime lastUpdatedTime;

    public void save(SensorData sensorData) {
        this.sensorData = sensorData;
        lastUpdatedTime = LocalDateTime.now();
    }

    public SensorData findData() {
        return this.sensorData;
    }

    public LocalDateTime findLastUpdatedTime() {
        return this.lastUpdatedTime;
    }
}
