package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(@RequestBody GPSLocationDtoNoIdNoType location) throws IOException {
        return weatherService.getWeatherOnNearestStationFromGivenLocation(location);
    }
}
