package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import org.springframework.stereotype.Component;

@Component
public class WeatherMapper {

    public WeatherDtoNoId mapToWeatherDtoNoId(Weather weather) {
        return new WeatherDtoNoId(
                weather.getSourceId(),
                weather.getMeasuringTime(),
                weather.getWindSpeed(),
                weather.getWindDirection(),
                weather.getHumidity(),
                weather.getAirTemperature(),
                weather.getGroundTemperature(),
                weather.getPrecipitationType(),
                weather.getWeatherStation().getName()
        );
    }
}
