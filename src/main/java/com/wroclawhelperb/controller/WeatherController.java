package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/location")
    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(@RequestBody GPSLocationDtoNoIdNoType location)
            throws IOException {
        return weatherService.getWeatherOnNearestStationFromGivenLocation(location);
    }

    @GetMapping("/user/{id}")
    public WeatherDtoNoId getWeatherOnNearestStationFromUser(@PathVariable Long userId)
            throws UserNotFoundException, IOException {
        return weatherService.getWeatherOnNearestStationFromUser(userId);
    }
}
