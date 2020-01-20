package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.mapper.WeatherMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

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


    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocation location) throws IOException {
        return weatherMapper.mapToWeatherDtoNoId(
                (Weather) location.findNearest(weatherMapper.mapToWeatherList(CSVMapper.mapToWeatherList(sourceUrl)))
        );
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(GPSLocationDtoNoIdNoType locationDto) throws IOException {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getWeatherOnNearestStationFromGivenLocation(location);
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromLocable(Locable locable) throws IOException {
        return getWeatherOnNearestStationFromGivenLocation(locable.getLocation());
    }

    public WeatherDtoNoId getWeatherOnNearestStationFromUser(Long userId) throws UserNotFoundException, IOException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getWeatherOnNearestStationFromLocable(user);
    }
}

