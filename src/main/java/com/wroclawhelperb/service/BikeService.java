package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BikeService {

    private final UserRepository userRepository;
    private final CSVMapper csvMapper;

    @Value("${bike.station.api.endpoint}")
    private String sourceUrl;

    public BikeService(UserRepository userRepository, CSVMapper csvMapper) {
        this.userRepository = userRepository;
        this.csvMapper = csvMapper;
    }

    public List<BikeStationDto> getAllStations() {
        return csvMapper.mapToBikeStationList(sourceUrl);
    }

    public BikeStationDto getStationById(Long stationId) throws BikeStationNotFoundException {
        return csvMapper.mapToBikeStationList(sourceUrl)
                .stream()
                .filter(s -> s.getUniqueId().equals(stationId))
                .findFirst()
                .orElseThrow(BikeStationNotFoundException::new);
    }

    private BikeStationDto getNearestStationWithAvailableBike(GPSLocation location) {
        List<BikeStationDto> stationList = csvMapper.mapToBikeStationList(sourceUrl)
                .stream()
                .filter(s -> !s.getBikeList().isEmpty())
                .collect(Collectors.toList());
        return (BikeStationDto) location.findNearest(stationList);
    }

    public BikeStationDto getNearestStationWithAvailableBike(GPSLocationDtoNoIdNoType locationDto) {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getNearestStationWithAvailableBike(location);
    }

    public BikeStationDto getNearestStationWithAvailableBike(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getNearestStationWithAvailableBike(user.getLocation());
    }
}
