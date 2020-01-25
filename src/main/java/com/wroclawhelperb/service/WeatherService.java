package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.mapper.WeatherMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherService {
    private final WeatherMapper weatherMapper;
    private final UserRepository userRepository;


    @Value("${weather.api.endpoint}")
    private String sourceUrl;

    public WeatherService(WeatherMapper weatherMapper, UserRepository userRepository) {
        this.weatherMapper = weatherMapper;
        this.userRepository = userRepository;
    }


    public List<WeatherDtoNoId> getWeatherOnAllStations() {
        return CSVMapper.mapToWeatherList(sourceUrl);
    }

    public WeatherDtoNoId getWeatherOnStation(String stationId) throws WeatherStationNotFoundException {
        return CSVMapper.mapToWeatherList(sourceUrl).stream()
                .filter(w -> w.getWeatherStationName().equals(stationId.toUpperCase()))
                .findFirst()
                .orElseThrow(WeatherStationNotFoundException::new);
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocation location) {
        return weatherMapper.mapToWeatherDto(
                (Weather) location.findNearest(weatherMapper.mapToWeatherList(CSVMapper.mapToWeatherList(sourceUrl)))
        );
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocationDtoNoIdNoType locationDto) {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getWeatherOnNearestStationFromGivenLocation(location);
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromLocable(Locable locable) {
        return getWeatherOnNearestStationFromGivenLocation(locable.getLocation());
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromUser(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getWeatherOnNearestStationFromLocable(user);
    }
}

