package com.wroclawhelperb.domain.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WeatherDtoNoId {

    private LocalDateTime measuringTime;
    private double windSpeed;
    private double windDirection;
    private double humidity;
    private double airTemperature;
    private double groundTemperature;
    private String precipitationType;
    private String weatherStationName;

    @Override
    public String toString() {
        return "WeatherDtoNoId{" +
                ", measuringTime=" + measuringTime +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", humidity=" + humidity +
                ", airTemperature=" + airTemperature +
                ", groundTemperature=" + groundTemperature +
                ", precipitationType='" + precipitationType + '\'' +
                ", weatherStation=" + weatherStationName +
                '}';
    }

    public String getStringWindDirection() {
        if (windDirection >= 0 && windDirection <= 22.5
                || windDirection > 337.5 && windDirection <= 360) {
            return "N";
        } else if (windDirection > 22.5 && windDirection <= 67.5) {
            return "NE";
        } else if (windDirection > 67.5 && windDirection <= 112.5) {
            return "E";
        } else if (windDirection > 112.5 && windDirection <= 157.5) {
            return "SE";
        } else if (windDirection > 157.5 && windDirection <= 202.5) {
            return "S";
        } else if (windDirection > 202.5 && windDirection <= 247.5) {
            return "SW";
        } else if (windDirection > 247.5 && windDirection <= 292.5) {
            return "W";
        } else if (windDirection > 292.5 && windDirection <= 337.5) {
            return "NW";
        }
        return "UNSPECIFIED DIRECTION";
    }

    public String getEnglishPrecipitationType() {
        switch (precipitationType) {
            case "Brak opadu":
                return "NONE";
            case "Opad przelotny":
                return "SHOWER";
            case "Opad ciągły":
                return "CONTINUOUS";
            case "Opad intensywny":
                return "HEAVY";
            default:
                return "UNSPECIFIED PRECIPITATION TYPE";
        }
    }
}
