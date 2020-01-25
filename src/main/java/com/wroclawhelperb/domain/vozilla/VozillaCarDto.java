package com.wroclawhelperb.domain.vozilla;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class VozillaCarDto implements Locable {

    @JsonProperty("platesNumber")
    private String platesNumber;

    @JsonProperty("sideNumber")
    private String sideNumber;

    @JsonProperty("color")
    private String color;

    @JsonProperty("type")
    private String type;

    @JsonProperty("rangeKm")
    private int rangeKm;

    @JsonProperty("batteryLevelPct")
    private int batteryLevelPct;

    @JsonProperty("status")
    private String status;

    @JsonProperty("address")
    private String address;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private GPSLocation location;

    @Override
    public String toString() {
        return "VozillaCarDto{" +
                "platesNumber='" + platesNumber + '\'' +
                ", sideNumber='" + sideNumber + '\'' +
                ", color='" + color + '\'' +
                ", type='" + type + '\'' +
                ", rangeKm=" + rangeKm +
                ", batteryLevelPct=" + batteryLevelPct +
                ", status='" + status + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
