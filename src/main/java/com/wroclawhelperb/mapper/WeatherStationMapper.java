package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.domain.weather.WeatherStationDto;
import org.springframework.stereotype.Component;

@Component
public class WeatherStationMapper {

    public WeatherStationDto mapToWeatherStationDto(WeatherStation station) {
        return new WeatherStationDto(station.getShortName(), station.getName(), station.getLocation());
    }
}
