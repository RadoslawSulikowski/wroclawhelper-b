package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.BikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {

    private final BikeService bikeService;

    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @GetMapping
    public List<BikeStationDto> getAllStations() {
        return bikeService.getAllStations();
    }

    @GetMapping(value = "/{stationId}")
    public BikeStationDto getStationWithGivenId(@PathVariable Long stationId) throws BikeStationNotFoundException {
        return bikeService.getStationById(stationId);
    }

    @GetMapping(value = "/location")
    public BikeStationDto getNearestStationWithAvailableBikeFromGivenLocation(@RequestBody GPSLocationDtoNoIdNoType location) {
        return bikeService.getNearestStationWithAvailableBike(location);
    }

    @GetMapping(value = "/user/{userId}")
    public BikeStationDto getNearestStationWithAvailableBikeFromUser(@PathVariable Long userId) throws UserNotFoundException {
        return bikeService.getNearestStationWithAvailableBike(userId);
    }
}
