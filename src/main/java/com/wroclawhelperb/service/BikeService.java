package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {

    @Value("${bike.station.api.endpoint}")
    private String sourceUrl;


    public BikeStationDto getNearestStationWithAvailableBike(GPSLocation location) {
        List<BikeStationDto> stationList = CSVMapper.mapToBikeStationList(sourceUrl)
                .stream()
                .filter(s -> !s.getBikeList().isEmpty())
                .collect(Collectors.toList());
        return (BikeStationDto) location.findNearest(stationList);
    }

    public BikeStationDto getNearestStationWithAvailableBike(GPSLocationDtoNoIdNoType locationDto) {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getNearestStationWithAvailableBike(location);
    }

    public List<BikeStationDto> getAllStations() {
        return CSVMapper.mapToBikeStationList(sourceUrl);    }

    public BikeStationDto geTStationById(Long stationId) throws BikeStationNotFoundException {
        return CSVMapper.mapToBikeStationList(sourceUrl)
                .stream()
                .filter(s -> s.getUniqueId().equals(stationId))
                .findFirst()
                .orElseThrow(BikeStationNotFoundException::new);
    }
}
