package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.service.WeatherStationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/weatherstations")
public class WeatherStationController {

    private final WeatherStationService weatherStationService;

    public WeatherStationController(WeatherStationService weatherStationService) {
        this.weatherStationService = weatherStationService;
    }

    @GetMapping
    public List<WeatherStationDto> getStations() {
        return weatherStationService.getWeatherStations();
    }
}
