package im.greenmate.api.domain.equipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SensorData {
    @JsonProperty("Module_ID")
    private String Module_ID;

    @JsonProperty("SenSorDatas")
    private List<SenSorData> SenSorDatas = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Module_ID = ");
        sb.append(Module_ID);
        sb.append("\n=== SenSorDataList ===\n");
        for (SenSorData senSorData : SenSorDatas) {
            sb.append("{\n");
            sb.append(senSorData.toString());
            sb.append("\n},\n");
        }
        return sb.toString();
    }

    @Getter
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

        @Override
        public String toString() {
            return "\tYear = " + Year +
                    "\n\tMonth = " + Month +
                    "\n\tDay = " + Day +
                    "\n\tHour = " + Hour +
                    "\n\tMin = " + Min +
                    "\n\tTemp = " + Temp +
                    "\n\tHumi = " + Humi +
                    "\n\tLight = " + Light +
                    "\n\tGroundHumi = " + GroundHumi;
        }
    }
}
