package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
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
                1L, LocalDateTime.of(2020, 1, 20, 22, 40),
                10.0, 180, 50, 10, 20,
                TEST_PRECIPITATION_TYPE_1, MILENIJNY);
        WeatherDtoNoId weather2 = new WeatherDtoNoId(
                3L, LocalDateTime.of(2020, 1, 21, 2, 0),
                0.0, 240, 90, 2, -3.5,
                TEST_PRECIPITATION_TYPE_2, OPOLSKA);
        WeatherDtoNoId weather3 = new WeatherDtoNoId(
                8L, LocalDateTime.of(2020, 1, 20, 2, 44),
                2.5, 30, 100, -2.4, -8,
                TEST_PRECIPITATION_TYPE_3, LOTNICZA);
        weatherList.add(weather1);
        weatherList.add(weather2);
        weatherList.add(weather3);
        when(weatherService.getWeatherOnAllStations()).thenReturn(weatherList);

        //When & Then
        mockMvc.perform(get("/weather").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].sourceId", is(1)))
                .andExpect(jsonPath("$[1].measuringTime", is("2020-01-21T02:00:00")))
                .andExpect(jsonPath("$[2].windSpeed", is(2.5)))
                .andExpect(jsonPath("$[0].windDirection", is((double) 180)))
                .andExpect(jsonPath("$[1].humidity", is((double) 90)))
                .andExpect(jsonPath("$[2].airTemperature", is(-2.4)))
                .andExpect(jsonPath("$[0].groundTemperature", is((double) 20)))
                .andExpect(jsonPath("$[1].precipitationType", is(TEST_PRECIPITATION_TYPE_2)))
                .andExpect(jsonPath("$[2].weatherStationName", is(LOTNICZA)));
    }


}
