package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.service.WeatherService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping()
    public List<WeatherDtoNoId> getWeatherOnAllStations()
            throws IOException {
        return weatherService.getWeatherOnAllStations();
    }

    @GetMapping(value = "/{stationId}")
    public WeatherDtoNoId getWeatherOnStation(@PathVariable String stationId)
            throws IOException, WeatherStationNotFoundException {
        return weatherService.getWeatherOnStation(stationId);
    }

    @GetMapping(value = "/location")
    public WeatherDtoNoId getWeatherOnNearestStationFromGivenLocation(@RequestBody GPSLocationDtoNoIdNoType location)
            throws IOException {
        return weatherService.getWeatherOnNearestStationFromGivenLocation(location);
    }

    @GetMapping("/user/{userId}")
    public WeatherDtoNoId getWeatherOnNearestStationFromUser(@PathVariable Long userId)
            throws UserNotFoundException, IOException {
        return weatherService.getWeatherOnNearestStationFromUser(userId);
    }
}
