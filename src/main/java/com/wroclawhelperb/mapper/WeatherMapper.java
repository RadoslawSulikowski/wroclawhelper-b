package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherMapper {

    private final WeatherStationRepository weatherStationRepository;

    public WeatherMapper(WeatherStationRepository weatherStationRepository) {
        this.weatherStationRepository = weatherStationRepository;
    }

    public WeatherDtoNoId mapToWeatherDto(Weather weather) {
        return new WeatherDtoNoId(weather.getMeasuringTime(), weather.getWindSpeed(), weather.getWindDirection(),
                weather.getHumidity(), weather.getAirTemperature(), weather.getGroundTemperature(),
                weather.getPrecipitationType(), weather.getWeatherStation().getName());
    }

    public Weather mapToWeather(WeatherDtoNoId weatherDto) {
        return new Weather(weatherDto.getMeasuringTime(), weatherDto.getWindSpeed(), weatherDto.getWindDirection(),
                weatherDto.getHumidity(), weatherDto.getAirTemperature(), weatherDto.getGroundTemperature(),
                weatherDto.getPrecipitationType(), weatherStationRepository.findById(weatherDto.getWeatherStationName())
                .orElse(new WeatherStation("UNKNOWN", "UNKNOWN",
                        new GPSLocation(51.109462, 17.032703, "WEATHER_STATION_LOCATION"))));
    }

    public List<Weather> mapToWeatherList(List<WeatherDtoNoId> weatherDtoList) {
        return weatherDtoList.stream().map(this::mapToWeather).collect(Collectors.toList());
    }
}
