package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.exception.NoStationIdInMapException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.mapper.WeatherStationMapper;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Double.parseDouble;

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

    public WeatherStationDto addNewWeatherStation(WeatherStationDto station) {
        return weatherStationMapper.mapToWeatherStationDto(
                weatherStationRepository.save(weatherStationMapper.mapToWeatherStation(station)));
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

    public WeatherStationDto updateStationProperty(Map<String, String> propertyNameValueMap)
            throws WeatherStationNotFoundException, NoStationIdInMapException {
        String stationId = propertyNameValueMap.entrySet()
                .stream()
                .filter(e -> e.getKey().equals("shortName"))
                .findFirst().orElseThrow(NoStationIdInMapException::new).getValue();
        WeatherStation station = weatherStationRepository.findById(stationId).orElseThrow(WeatherStationNotFoundException::new);
        for (Map.Entry<String, String> e : propertyNameValueMap.entrySet()) {
            if (e.getKey().equals("name")) {
                station.setName(e.getValue());
            }
            if (e.getKey().equals("latitude")) {
                station.getLocation().setLatitude(parseDouble(e.getValue()));
            }
            if (e.getKey().equals("longitude")) {
                station.getLocation().setLongitude(parseDouble(e.getValue()));
            }
        }
        return weatherStationMapper.mapToWeatherStationDto(weatherStationRepository.save(station));
    }
}
