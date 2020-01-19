package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.mapper.WeatherMapper;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class WeatherService {

    private final WeatherStationRepository weatherStationRepository;
    private final WeatherMapper weatherMapper;
    private final String SOURCE_URL = "https://www.wroclaw.pl/open-data/datastore/dump/9d5b2336-6f9a-4fa0-8cbe-d6b4776194c3";

    public WeatherService(WeatherStationRepository weatherStationRepository, WeatherMapper weatherMapper) {
        this.weatherStationRepository = weatherStationRepository;
        this.weatherMapper = weatherMapper;
    }


    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocation location) throws IOException {
        CSVMapper csvMapper = new CSVMapper(weatherStationRepository);
        List<Weather> weatherList = csvMapper.mapToWeatherList(SOURCE_URL);
        return weatherMapper.mapToWeatherDtoNoId((Weather) location.findNearest(weatherList));
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocationDtoNoIdNoType locationDto) throws IOException {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getWeatherOnNearestStationFromGivenLocation(location);
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromLocable(Locable locable) throws IOException {
        return getWeatherOnNearestStationFromGivenLocation(locable.getLocation());
    }
}

