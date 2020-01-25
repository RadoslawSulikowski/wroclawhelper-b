package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class WeatherServiceTestSuite {

    @Autowired
    private WeatherService weatherService;

    @MockBean
    UserRepository userRepository;

    @Test

    public void testGetWeather() throws UserNotFoundException {
        //Given
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(51.138235, 16.973045, GPSLocation.USER_FAVORITE_LOCATION));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //When
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStation(1L);

        //Then
        assertEquals("MOST MILENIJNY", weather.getWeatherStationName());
    }
}
