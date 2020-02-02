package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
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

    public WeatherStationDto updateStation(WeatherStationDto station) throws WeatherStationNotFoundException {
        WeatherStation weatherStation = weatherStationRepository.findById(station.getShortName())
                .orElseThrow(WeatherStationNotFoundException::new);
        weatherStation.setName(station.getName());
        weatherStation.getLocation().setLatitude(station.getLocation().getLatitude());
        weatherStation.getLocation().setLongitude(station.getLocation().getLongitude());
        return weatherStationMapper.mapToWeatherStationDto(
                weatherStationRepository.save(weatherStation));
    }
}
