package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.exception.CarNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.VozillaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class VozillaController {

    private final VozillaService vozillaService;

    public VozillaController(VozillaService vozillaService) {
        this.vozillaService = vozillaService;
    }

    @GetMapping
    public List<VozillaCarDto> getAllCars() {
        return vozillaService.getVozillaCarList();
    }

    @GetMapping("/{platesNumber}")
    public VozillaCarDto getCarByPlatesNumber(@PathVariable String platesNumber)
            throws CarNotFoundException {
        return vozillaService.getCarByPlatesNumber(platesNumber);
    }

    @GetMapping("/location")
    public VozillaCarDto getNearestAvailableCarFromGivenLocation(@RequestBody GPSLocationDtoNoIdNoType location) {
        return vozillaService.getNearestAvailableCar(location);
    }

    @GetMapping("/{userId}")
    public VozillaCarDto getNearestAvailableCarFromUser(@PathVariable Long userId)
            throws UserNotFoundException {
        return vozillaService.getNearestAvailableCar(userId);
    }
}
