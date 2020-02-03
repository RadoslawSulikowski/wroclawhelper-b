package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class BikeServiceTestSuite {

    @Autowired
    private BikeService service;

    @MockBean
    private CSVMapper mapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldFetchBikeStationList() {
        //Given
        BikeStationDto station1 = new BikeStationDto(1L, 1, 1, 2,
                Arrays.asList(1, 2), new GPSLocation(), "1");
        BikeStationDto station2 = new BikeStationDto(2L, 2, 2, 4,
                Arrays.asList(1, 2, 3, 4), new GPSLocation(), "2");
        List<BikeStationDto> stations = Arrays.asList(station1, station2);
        when(mapper.mapToBikeStationList(anyString())).thenReturn(stations);

        //When
        List<BikeStationDto> resultList = service.getAllStations();

        //Then
        assertEquals(2, resultList.size());
        assertEquals(station1, resultList.get(0));
        assertEquals(station2, resultList.get(1));
    }

    @Test
    void shouldFetchEmptyList() {
        //Given
        when(mapper.mapToBikeStationList(anyString())).thenReturn(new ArrayList<>());

        //When
        List<BikeStationDto> resultList = service.getAllStations();

        //Then
        assertEquals(0, resultList.size());
    }

    @Test
    void shouldGetStationByIdThrowBikeStationNotFoundException() {
        //Given
        when(mapper.mapToBikeStationList(anyString())).thenReturn(new ArrayList<>());

        //When & Then
        assertThrows(BikeStationNotFoundException.class, () -> service.getStationById(anyLong()));
    }

    @Test
    void shouldGetStationByIdReturnStation() throws BikeStationNotFoundException {
        //Given
        BikeStationDto station1 = new BikeStationDto(1L, 1, 1, 2,
                Arrays.asList(1, 2), new GPSLocation(), "1");
        BikeStationDto station2 = new BikeStationDto(2L, 2, 2, 4,
                Arrays.asList(1, 2, 3, 4), new GPSLocation(), "2");
        List<BikeStationDto> stations = Arrays.asList(station1, station2);
        when(mapper.mapToBikeStationList(anyString())).thenReturn(stations);

        //When
        BikeStationDto resultStation = service.getStationById(1L);

        //Then
        assertEquals(station1, resultStation);
    }

    @Test
    void shouldGetNearestStationWithAvailableBikeThrowUserNotFoundException() {
        //Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.getNearestStationWithAvailableBike(anyLong()));
    }

    @Test
    void shouldGetNearestStationWithAvailableBikeByUserIdReturnStation() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setLocation(new GPSLocation(1.0, 1.5));
        BikeStationDto station1 = new BikeStationDto(1L, 1, 1, 2,
                Arrays.asList(1, 2), new GPSLocation(5.0, 5.0), "1");
        BikeStationDto station2 = new BikeStationDto(2L, 2, 2, 4,
                Arrays.asList(1, 2, 3, 4), new GPSLocation(1.1, 1.4), "2");
        List<BikeStationDto> stations = Arrays.asList(station1, station2);
        when(mapper.mapToBikeStationList(anyString())).thenReturn(stations);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        //When
        BikeStationDto resultStation = service.getNearestStationWithAvailableBike(anyLong());

        //Then
        assertEquals(station2, resultStation);
    }

    @Test
    void shouldGetNearestStationWithAvailableBikeByLocationDtoReturnStation() {
        //Given
        GPSLocationDtoNoIdNoType givenLocation = new GPSLocationDtoNoIdNoType(4.8, 5.1);
        BikeStationDto station1 = new BikeStationDto(1L, 1, 1, 2,
                Arrays.asList(1, 2), new GPSLocation(5.0, 5.0), "1");
        BikeStationDto station2 = new BikeStationDto(2L, 2, 2, 4,
                Arrays.asList(1, 2, 3, 4), new GPSLocation(1.1, 1.4), "2");
        List<BikeStationDto> stations = Arrays.asList(station1, station2);
        when(mapper.mapToBikeStationList(anyString())).thenReturn(stations);

        //When
        BikeStationDto resultStation = service.getNearestStationWithAvailableBike(givenLocation);

        //Then
        assertEquals(station1, resultStation);
    }
}
