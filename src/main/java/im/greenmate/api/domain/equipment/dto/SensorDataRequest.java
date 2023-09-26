package im.greenmate.api.domain.equipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SensorDataRequest {
    @JsonProperty("Module_ID")
    private String Module_ID;

    @JsonProperty("SenSorDatas")
    private List<SenSorData> SenSorDatas = new ArrayList<>();

    @Getter
    @ToString
    @NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
    static class SenSorData {
        @JsonProperty("Year")
        private int Year;
        @JsonProperty("Month")
        private int Month;
        @JsonProperty("Day")
        private int Day;
        @JsonProperty("Hour")
        private int Hour;
        @JsonProperty("Min")
        private int Min;
        @JsonProperty("Temp")
        private int Temp;
        @JsonProperty("Humi")
        private int Humi;
        @JsonProperty("Light")
        private int Light;
        @JsonProperty("GroundHumi")
        private int GroundHumi;
    }
}
