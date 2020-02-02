package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.domain.vozilla.VozillaCarDtoList;
import com.wroclawhelperb.exception.CarNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class VozillaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VozillaService.class);

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;

    @Value("${vozilla.car.api.endpoint}")
    String sourceUrl;


    public VozillaService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    private URI createUri() {
        return UriComponentsBuilder.fromHttpUrl(sourceUrl).build().encode().toUri();
    }

    public List<VozillaCarDto> getVozillaCarList() {
        try {
            return ofNullable(restTemplate.getForObject(createUri(), VozillaCarDtoList.class).getCars()).orElse(new ArrayList<>());

        } catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public VozillaCarDto getCarByPlatesNumber(String platesNumber) throws CarNotFoundException {
        return getVozillaCarList()
                .stream()
                .filter(c -> c.getPlatesNumber().equals(platesNumber))
                .findFirst()
                .orElseThrow(CarNotFoundException::new);
    }

    private VozillaCarDto getNearestAvailableCar(GPSLocation location) {
        return (VozillaCarDto) location.findNearest(getVozillaCarList()
                .stream()
                .filter(c -> c.getStatus().equals("AVAILABLE"))
                .collect(Collectors.toList()));
    }

    public VozillaCarDto getNearestAvailableCar(GPSLocationDtoNoIdNoType locationDto) {
        GPSLocation location = new GPSLocation(locationDto.getLatitude(), locationDto.getLongitude());
        return getNearestAvailableCar(location);
    }

    public VozillaCarDto getNearestAvailableCar(Long userId) throws UserNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return getNearestAvailableCar(user.getLocation());

    }
}
