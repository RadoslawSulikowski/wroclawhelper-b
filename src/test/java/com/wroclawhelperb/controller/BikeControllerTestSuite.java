package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.exception.BikeStationNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.BikeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BikeController.class)
public class BikeControllerTestSuite {

    private static final String STATION_NAME_1 = "STATION_NAME_1";
    private static final String STATION_NAME_2 = "STATION_NAME_2";
    private static final String STATION_NAME_3 = "STATION_NAME_3";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BikeService service;


    @Test
    public void shouldFetchBikeStationList() throws Exception {
        //Given
        when(service.getAllStations()).thenReturn(prepareBikeList());

        //When & Then
        mockMvc.perform(get("/bikes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].uniqueId", is(1)))
                .andExpect(jsonPath("$[1].number", is(2)))
                .andExpect(jsonPath("$[2].bookedBikes", is(0)))
                .andExpect(jsonPath("$[0].bikes", is(3)))
                .andExpect(jsonPath("$[1].bikeList", hasSize(3)))
                .andExpect(jsonPath("$[2].bikeList", hasSize(0)))
                .andExpect(jsonPath("$[0].name", is(STATION_NAME_1)))
                .andExpect(jsonPath("$[1].location.latitude", is(2.0)))
                .andExpect(jsonPath("$[2].location.longitude", is(3.5)));
    }

    @Test
    public void shouldHandleBikeStationNotFoundException() throws Exception {
        //Given
        when(service.getStationById(anyLong())).thenThrow(new BikeStationNotFoundException());

        //When & Then
        mockMvc.perform(get("/bikes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No Bike Station with given id"));
    }

    @Test
    public void shouldFetchBikeStationById() throws Exception {
        //Given
        BikeStationDto station = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), STATION_NAME_1);
        when(service.getStationById(anyLong())).thenReturn(station);

        //When & Then
        mockMvc.perform(get("/bikes/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.uniqueId", is(1)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.bookedBikes", is(1)))
                .andExpect(jsonPath("$.bikes", is(3)))
                .andExpect(jsonPath("$.bikeList", hasSize(3)))
                .andExpect(jsonPath("$.bikeList[1]", is(456)))
                .andExpect(jsonPath("$.location.longitude", is(1.5)))
                .andExpect(jsonPath("$.name", is(STATION_NAME_1)));
    }

    @Test
    public void shouldHandleUserNotFoundException() throws Exception {
        //Given
        when(service.getNearestStationWithAvailableBike(anyLong())).thenThrow(new UserNotFoundException());

        //When & Then
        mockMvc.perform(get("/bikes/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    public void shouldFetchBikeStationConsumesLocation() throws Exception {
        //Given
        BikeStationDto station = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), STATION_NAME_1);
        when(service.getNearestStationWithAvailableBike(any(GPSLocationDtoNoIdNoType.class))).thenReturn(station);
        Gson gson = new Gson();
        String jsonContent = gson.toJson(new GPSLocationDtoNoIdNoType(52.0, 17.0));

        //When & Then
        mockMvc.perform(get("/bikes/location")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonContent))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.uniqueId", is(1)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.bookedBikes", is(1)))
                .andExpect(jsonPath("$.bikes", is(3)))
                .andExpect(jsonPath("$.bikeList", hasSize(3)))
                .andExpect(jsonPath("$.bikeList[1]", is(456)))
                .andExpect(jsonPath("$.location.longitude", is(1.5)))
                .andExpect(jsonPath("$.name", is(STATION_NAME_1)));
    }

    @Test
    public void shouldFetchBikeStationConsumesUserId() throws Exception {
        //Given
        BikeStationDto station = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), STATION_NAME_1);
        when(service.getNearestStationWithAvailableBike(anyLong())).thenReturn(station);

        //When & Then
        mockMvc.perform(get("/bikes/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.uniqueId", is(1)))
                .andExpect(jsonPath("$.number", is(1)))
                .andExpect(jsonPath("$.bookedBikes", is(1)))
                .andExpect(jsonPath("$.bikes", is(3)))
                .andExpect(jsonPath("$.bikeList", hasSize(3)))
                .andExpect(jsonPath("$.bikeList[1]", is(456)))
                .andExpect(jsonPath("$.location.longitude", is(1.5)))
                .andExpect(jsonPath("$.name", is(STATION_NAME_1)));
    }

    private List<BikeStationDto> prepareBikeList() {
        BikeStationDto station1 = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), STATION_NAME_1);
        BikeStationDto station2 = new BikeStationDto(2L, 2, 2, 3,
                new ArrayList<>(Arrays.asList(987, 654, 321)),
                new GPSLocation(2.0, 2.5), STATION_NAME_2);
        BikeStationDto station3 = new BikeStationDto(3L, 3, 0, 0,
                new ArrayList<>(),
                new GPSLocation(3.0, 3.5), STATION_NAME_3);
        return new ArrayList<>(Arrays.asList(station1, station2, station3));
    }
}
