package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.mapper.CSVMapper;
import com.wroclawhelperb.mapper.WeatherMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
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
class WeatherServiceTestSuite {

    @Autowired
    private WeatherService weatherService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CSVMapper csvMapper;

    @MockBean
    private WeatherMapper weatherMapper;

    private static final LocalDateTime MEASURING_TIME
            = LocalDateTime.of(2020, 2, 2, 2, 22, 22);
    private static final double HARD_WIND_SPEED = 9.1;
    private static final double LIGHT_WIND_SPEED = 2.4;
    private static final double WIND_DIRECTION = 22.5;
    private static final double HUMIDITY = 74.4;
    private static final double AIR_TEMPERATURE_FREEZE = -11.1;
    private static final double AIR_TEMPERATURE_NO_FREEZE = 2.4;
    private static final double GROUND_TEMPERATURE = 12.4;
    private static final String PRECIPITATION_TYPE_CONTINUOUS = "Opad ciągły";
    private static final String PRECIPITATION_TYPE_NONE = "Brak opadu";

    @Test
    void shouldGetWeatherOnAllStationsReturnWeatherList() {
        //Given
        WeatherDtoNoId weather1 = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, "MOST MILENIJNY");
        WeatherDtoNoId weather2 = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, "MOST WARSZAWSKI");
        List<WeatherDtoNoId> weatherList = new ArrayList<>(Arrays.asList(weather1, weather2));
        when(csvMapper.mapToWeatherList(anyString())).thenReturn(weatherList);

        //When
        List<WeatherDtoNoId> resultList = weatherService.getWeatherOnAllStations();

        //Then
        assertEquals(weatherList, resultList);
    }

    @Test
    void shouldGetWeatherOnNearestStationByUserIdReturnWeather() throws UserNotFoundException {
        //Given
        WeatherDtoNoId weatherDto1 = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, "MOST MILENIJNY");
        WeatherDtoNoId weatherDto2 = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, "MOST WARSZAWSKI");
        List<WeatherDtoNoId> weatherDtoList = new ArrayList<>(Arrays.asList(weatherDto1, weatherDto2));
        Weather weather1 = new Weather(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE,
                new WeatherStation("MILENIJNY", "MOST MILENIJNY",
                        new GPSLocation(51.131199, 16.984879)));
        Weather weather2 = new Weather(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS,
                new WeatherStation("WARSZAWSKI", "MOST WARSZAWSKI",
                        new GPSLocation(51.128788, 17.056380)));
        List<Weather> weatherList = new ArrayList<>(Arrays.asList(weather1, weather2));
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(51.138235, 16.973045, GPSLocation.USER_FAVORITE_LOCATION), true);
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(csvMapper.mapToWeatherList(anyString())).thenReturn(weatherDtoList);
        when(weatherMapper.mapToWeatherList(weatherDtoList)).thenReturn(weatherList);
        when(weatherMapper.mapToWeatherDto(weather1)).thenReturn(weatherDto1);

        //When
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStation(1L);

        //Then
        assertEquals(weatherDto1, weather);
    }

    @Test
    void shouldGetWeatherOnNearestStationByLocationDtoReturnWeather() throws UserNotFoundException {
        //Given
        WeatherDtoNoId weatherDto1 = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, "MOST MILENIJNY");
        WeatherDtoNoId weatherDto2 = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, "MOST WARSZAWSKI");
        List<WeatherDtoNoId> weatherDtoList = new ArrayList<>(Arrays.asList(weatherDto1, weatherDto2));
        Weather weather1 = new Weather(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE,
                new WeatherStation("MILENIJNY", "MOST MILENIJNY",
                        new GPSLocation(51.131199, 16.984879)));
        Weather weather2 = new Weather(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS,
                new WeatherStation("WARSZAWSKI", "MOST WARSZAWSKI",
                        new GPSLocation(51.128788, 17.056380)));
        List<Weather> weatherList = new ArrayList<>(Arrays.asList(weather1, weather2));
        GPSLocationDtoNoIdNoType location = new GPSLocationDtoNoIdNoType(51.138235, 16.973045);

        when(csvMapper.mapToWeatherList(anyString())).thenReturn(weatherDtoList);
        when(weatherMapper.mapToWeatherList(weatherDtoList)).thenReturn(weatherList);
        when(weatherMapper.mapToWeatherDto(weather1)).thenReturn(weatherDto1);

        //When
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStation(location);

        //Then
        assertEquals(weatherDto1, weather);
    }

    @Test
    void shouldGetWeatherOnNearestStationThrowUserNotFoundException() {
        //Given
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> weatherService.getWeatherOnNearestStation(anyLong()));
    }

    @Test
    void shouldGetWeatherOnStationThrowWeatherStationNotFoundException() {
        //Given
        when(csvMapper.mapToWeatherList((anyString()))).thenReturn(new ArrayList<>());

        //When & Then
        assertThrows(WeatherStationNotFoundException.class,
                () -> weatherService.getWeatherOnStation(anyString()));
    }

    @Test
    void shouldGetWeatherOnStationReturnWeather() throws WeatherStationNotFoundException {
        //Given
        WeatherDtoNoId weather1 = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, "MOST MILENIJNY");
        WeatherDtoNoId weather2 = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, "MOST WARSZAWSKI");
        List<WeatherDtoNoId> weatherList = new ArrayList<>(Arrays.asList(weather1, weather2));
        when(csvMapper.mapToWeatherList((anyString()))).thenReturn(weatherList);

        //When
        WeatherDtoNoId resultWeather = weatherService.getWeatherOnStation("MOST WARSZAWSKI");

        //Then
        assertEquals(weather2, resultWeather);
    }
}
