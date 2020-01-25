package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WeatherServiceTestSuite {

    @Autowired
    private WeatherService weatherService;

    @Test

    public void testGetWeather() {
        //Given
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(51.138235, 16.973045, GPSLocation.USER_FAVORITE_LOCATION));
        //When
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStationFromLocable(user);

        //Then
        assertEquals("MOST MILENIJNY", weather.getWeatherStationName());
    }
}
