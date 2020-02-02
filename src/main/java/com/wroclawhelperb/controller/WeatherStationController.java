package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.service.WeatherStationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Weather station successful added")
    public void addNewWeatherStation(@RequestBody WeatherStationDto station) {
        weatherStationService.addNewWeatherStation(station);
    }

    @PutMapping
    public WeatherStationDto updateStation(@RequestBody WeatherStationDto station) throws WeatherStationNotFoundException {
        return weatherStationService.updateStation(station);
    }
}
