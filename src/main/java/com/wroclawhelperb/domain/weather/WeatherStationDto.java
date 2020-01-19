package com.wroclawhelperb.domain.weather;

import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class WeatherStationDto {

    private String shortName;
    private String name;
    private GPSLocation location;

    @Override
    public String toString() {
        return "WeatherStationDto{" +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
