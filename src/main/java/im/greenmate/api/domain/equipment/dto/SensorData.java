package im.greenmate.api.domain.equipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class SensorData {

    @JsonProperty("Module_ID")
    private String Module_ID;

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
        StringBuilder sb = new StringBuilder();
        sb.append("Module_ID = ");
        sb.append(Module_ID);
        sb.append("\nTemp = ");
        sb.append(Temp);
        sb.append("\nHumi = ");
        sb.append(Humi);
        sb.append("\nLight = ");
        sb.append(Light);
        sb.append("\nGroundHumi = ");
        sb.append(GroundHumi);
        return sb.toString();
    }

}
