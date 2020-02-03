package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.exception.NoStationIdInMapException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.service.WeatherStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherStationController.class)
class WeatherStationControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherStationService service;

    @Test
    void shouldReturnEmptyList() throws Exception {
        //Given
        when(service.getWeatherStations()).thenReturn(new ArrayList<>());

        //When & Then
        mockMvc.perform(get("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldReturnWeatherStationList() throws Exception {
        //Given
        WeatherStationDto station1 = new WeatherStationDto("St1", "Station1",
                new GPSLocation(1, 1.5));
        WeatherStationDto station2 = new WeatherStationDto("St2", "Station2",
                new GPSLocation(2, 2.5));
        when(service.getWeatherStations()).thenReturn(new ArrayList<>(Arrays.asList(station1, station2)));

        //When & Then
        mockMvc.perform(get("/weatherstations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].shortName", is("St1")))
                .andExpect(jsonPath("$[0].name", is("Station1")))
                .andExpect(jsonPath("$[0].location.latitude", is(1.0)))
                .andExpect(jsonPath("$[0].location.longitude", is(1.5)))
                .andExpect(jsonPath("$[1].shortName", is("St2")))
                .andExpect(jsonPath("$[1].name", is("Station2")))
                .andExpect(jsonPath("$[1].location.latitude", is(2.0)))
                .andExpect(jsonPath("$[1].location.longitude", is(2.5)));
    }

    @Test
    void shouldCreatWeatherStation() throws Exception {
        //Given
        WeatherStationDto station = new WeatherStationDto("shortName", "name",
                new GPSLocation(1.0, 1.5));
        when(service.addNewWeatherStation(any(WeatherStationDto.class))).thenReturn(station);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(station);

        //When & Then
        mockMvc.perform(post("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(201))
                .andExpect(status().reason("Weather station successful added"));
    }

    @Test
    void shouldHandleWeatherStationNotFoundExceptionUpdateStation() throws Exception {
        //Given
        WeatherStationDto station = new WeatherStationDto("shortName", "name",
                new GPSLocation(1.0, 1.5));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(station);
        when(service.updateStation(any(WeatherStationDto.class))).thenThrow(new WeatherStationNotFoundException());

        //When & Then
        mockMvc.perform(put("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(404))
                .andExpect(status().reason("No Weather Station with given short name"));
    }

    @Test
    void shouldReturnUpdatedWeatherStation() throws Exception {
        //Given
        WeatherStationDto station = new WeatherStationDto("shortName", "name",
                new GPSLocation(1.0, 1.5));
        Gson gson = new Gson();
        String jsonContent = gson.toJson(station);
        when(service.updateStation(any(WeatherStationDto.class))).thenReturn(station);

        //When & Then
        mockMvc.perform(put("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.shortName", is("shortName")))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.location.latitude", is(1.0)))
                .andExpect(jsonPath("$.location.longitude", is(1.5)));
    }

    @Test
    void shouldUpdateStationPropertyThrowNoStationIdInMapException() throws Exception {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("someKey", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateStationProperty(anyMap())).thenThrow(NoStationIdInMapException.class);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(422))
                .andExpect(status().reason("Station short name not found in received data"));
    }

    @Test
    void shouldUpdateStationPropertyHandleWeatherStationNotFoundException() throws Exception {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateStationProperty(anyMap())).thenThrow(WeatherStationNotFoundException.class);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(404))
                .andExpect(status().reason("No Weather Station with given short name"));
    }

    @Test
    void shouldUpdateStationPropertyReturnUpdatedStation() throws Exception {
        //Given
        WeatherStationDto stationDto = new WeatherStationDto("shortName", "name",
                new GPSLocation(2.0, 3.0, GPSLocation.WEATHER_STATION_LOCATION));
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(service.updateStationProperty(anyMap())).thenReturn(stationDto);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(map);

        //When & Then
        mockMvc.perform(patch("/weatherstations")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.shortName", is("shortName")))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.location.latitude", is(2.0)))
                .andExpect(jsonPath("$.location.longitude", is(3.0)))
                .andExpect(jsonPath("$.location.locationType", is(GPSLocation.WEATHER_STATION_LOCATION)));
    }
}
