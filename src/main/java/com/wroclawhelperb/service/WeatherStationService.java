package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.mapper.WeatherStationMapper;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherStationService {

    private final WeatherStationRepository weatherStationRepository;
    private final WeatherStationMapper weatherStationMapper;

    public WeatherStationService(WeatherStationRepository weatherStationRepository, WeatherStationMapper weatherStationMapper) {
        this.weatherStationRepository = weatherStationRepository;
        this.weatherStationMapper = weatherStationMapper;
    }

    public List<WeatherStationDto> getWeatherStations() {
        List<WeatherStationDto> stations = new ArrayList<>();
        weatherStationRepository.findAll()
                .forEach(s -> {
            if (!s.getShortName().equals("UNKNOWN")) {
                stations.add(weatherStationMapper.mapToWeatherStationDto(s));
            }
        });
        return stations;
    }

    public void addNewWeatherStation(WeatherStationDto station) {
        weatherStationRepository.save(weatherStationMapper.mapToWeatherStation(station));
    }
}
