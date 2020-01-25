package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.wroclawhelperb.domain.weather.WeatherStation.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private static final String TEST_PRECIPITATION_TYPE_1 = "TEST PRECIPITATION TYPE 1";
    private static final String TEST_PRECIPITATION_TYPE_2 = "TEST PRECIPITATION TYPE 2";
    private static final String TEST_PRECIPITATION_TYPE_3 = "TEST PRECIPITATION TYPE 3";

    @Test
    public void shouldFetchWeatherDtoList() throws Exception {
        //Given
        List<WeatherDtoNoId> weatherList = new ArrayList<>();
        WeatherDtoNoId weather1 = new WeatherDtoNoId(
                LocalDateTime.of(2020, 1, 20, 22, 40),
                10.0, 180, 50, 10, 20,
                TEST_PRECIPITATION_TYPE_1, MILENIJNY);
        WeatherDtoNoId weather2 = new WeatherDtoNoId(
                LocalDateTime.of(2020, 1, 21, 2, 0),
                0.0, 240, 90, 2, -3.5,
                TEST_PRECIPITATION_TYPE_2, OPOLSKA);
        WeatherDtoNoId weather3 = new WeatherDtoNoId(
                LocalDateTime.of(2020, 1, 20, 2, 44),
                2.5, 30, 100, -2.4, -8,
                TEST_PRECIPITATION_TYPE_3, LOTNICZA);
        weatherList.add(weather1);
        weatherList.add(weather2);
        weatherList.add(weather3);
        when(weatherService.getWeatherOnAllStations()).thenReturn(weatherList);

        //When & Then
        mockMvc.perform(get("/weather")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].measuringTime", is("2020-01-21T02:00:00")))
                .andExpect(jsonPath("$[2].windSpeed", is(2.5)))
                .andExpect(jsonPath("$[0].windDirection", is((double) 180)))
                .andExpect(jsonPath("$[1].humidity", is((double) 90)))
                .andExpect(jsonPath("$[2].airTemperature", is(-2.4)))
                .andExpect(jsonPath("$[0].groundTemperature", is((double) 20)))
                .andExpect(jsonPath("$[1].precipitationType", is(TEST_PRECIPITATION_TYPE_2)))
                .andExpect(jsonPath("$[2].weatherStationName", is(LOTNICZA)));
    }

    @Test
    public void shouldHandleWeatherStationNotFoundException() throws Exception {
        //Given
        when(weatherService.getWeatherOnStation(anyString()))
                .thenThrow(WeatherStationNotFoundException.class);

        //When & Then
        mockMvc.perform(get("/weather/STATION_NAME")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No station with given id"));
    }

    @Test
    public void shouldFetchWeatherOnGivenStation() throws Exception {
        //Given
        String givenStationName = "Given_Station_Name";
        WeatherDtoNoId weather = new WeatherDtoNoId(LocalDateTime.of(2020, 1, 20, 22, 40),
                10.0, 180, 50, 10, 20,
                TEST_PRECIPITATION_TYPE_1, MILENIJNY);
        when(weatherService.getWeatherOnStation(givenStationName)).thenReturn(weather);

        //When & Then
        mockMvc.perform(get("/weather/" + givenStationName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("measuringTime", is("2020-01-20T22:40:00")))
                .andExpect(jsonPath("windSpeed", is((double) 10)))
                .andExpect(jsonPath("windDirection", is((double) 180)))
                .andExpect(jsonPath("humidity", is((double) 50)))
                .andExpect(jsonPath("airTemperature", is(((double) 10))))
                .andExpect(jsonPath("groundTemperature", is(((double) 20))))
                .andExpect(jsonPath("precipitationType", is(TEST_PRECIPITATION_TYPE_1)))
                .andExpect(jsonPath("weatherStationName", is(MILENIJNY)));
    }

    @Test
    public void shouldFetchWeatherOnNearestStationFromGivenLocation() throws Exception {
        //Given
        WeatherDtoNoId weather = new WeatherDtoNoId(
                LocalDateTime.of(2020, 1, 20, 22, 40),
                10.0, 180, 50, 10, 20,
                TEST_PRECIPITATION_TYPE_1, MILENIJNY);
        when(weatherService.getWeatherOnNearestStation(any(GPSLocationDtoNoIdNoType.class)))
                .thenReturn(weather);
        GPSLocationDtoNoIdNoType givenLocation = new GPSLocationDtoNoIdNoType(1.0, 1.0);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(givenLocation);

        //When & Then
        mockMvc.perform(get("/weather/location")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("measuringTime", is("2020-01-20T22:40:00")))
                .andExpect(jsonPath("windSpeed", is((double) 10)))
                .andExpect(jsonPath("windDirection", is((double) 180)))
                .andExpect(jsonPath("humidity", is((double) 50)))
                .andExpect(jsonPath("airTemperature", is(((double) 10))))
                .andExpect(jsonPath("groundTemperature", is(((double) 20))))
                .andExpect(jsonPath("precipitationType", is(TEST_PRECIPITATION_TYPE_1)))
                .andExpect(jsonPath("weatherStationName", is(MILENIJNY)));
    }

    @Test
    public void shouldHandleUserNotFoundException() throws Exception {
        //Given
        when(weatherService.getWeatherOnNearestStation(anyLong()))
                .thenThrow(UserNotFoundException.class);

        //When & Then
        mockMvc.perform(get("/weather/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    public void shouldFetchWeatherOnNearestStationFromUser() throws Exception {
        //Given
        WeatherDtoNoId weather = new WeatherDtoNoId(
                LocalDateTime.of(2020, 1, 20, 22, 40),
                10.0, 180, 50, 10, 20,
                TEST_PRECIPITATION_TYPE_1, MILENIJNY);
        when(weatherService.getWeatherOnNearestStation(anyLong()))
                .thenReturn(weather);

        //When & Then
        mockMvc.perform(get("/weather/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("measuringTime", is("2020-01-20T22:40:00")))
                .andExpect(jsonPath("windSpeed", is((double) 10)))
                .andExpect(jsonPath("windDirection", is((double) 180)))
                .andExpect(jsonPath("humidity", is((double) 50)))
                .andExpect(jsonPath("airTemperature", is(((double) 10))))
                .andExpect(jsonPath("groundTemperature", is(((double) 20))))
                .andExpect(jsonPath("precipitationType", is(TEST_PRECIPITATION_TYPE_1)))
                .andExpect(jsonPath("weatherStationName", is(MILENIJNY)));
    }
}
