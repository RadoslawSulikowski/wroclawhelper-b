package com.wroclawhelperb.controller;

import com.google.gson.Gson;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.exception.CarNotFoundException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.VozillaService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(VozillaController.class)
class VozillaControllerTestSuite {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VozillaService service;

    @Test
    void shouldFetchCarList() throws Exception {
        //Given
        when(service.getVozillaCarList()).thenReturn(prepareCarList());

        //When & Then
        mockMvc.perform(get("/cars").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].platesNumber", is("A")))
                .andExpect(jsonPath("$[1].sideNumber", is("BB")))
                .andExpect(jsonPath("$[2].color", is("CCC")))
                .andExpect(jsonPath("$[0].type", is("AAAA")))
                .andExpect(jsonPath("$[1].rangeKm", is(20)))
                .andExpect(jsonPath("$[2].batteryLevelPct", is(30)))
                .andExpect(jsonPath("$[0].status", is("a")))
                .andExpect(jsonPath("$[1].address", is("bb")))
                .andExpect(jsonPath("$[2].name", is("ccc")))
                .andExpect(jsonPath("$[1].location.latitude", is(51.5)))
                .andExpect(jsonPath("$[2].location.longitude", is(17.0)));
    }

    @Test
    void shouldHandleCarNotFoundException() throws Exception {
        //Given
        when(service.getCarByPlatesNumber(anyString())).thenThrow(new CarNotFoundException());

        //When & Then
        mockMvc.perform(get("/cars/plateNumber").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No car with given plates number"));
    }

    @Test
    void shouldFetchCarByPlatesName() throws Exception {
        //Given
        when(service.getCarByPlatesNumber(anyString())).thenReturn(new VozillaCarDto(
                "A", "AA", "AAA", "AAAA", 10, 10,
                "a", "aa", "aaa",
                new GPSLocation(51.0, 16.0)));

        //When & Then
        mockMvc.perform(get("/cars/plateNumber").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.platesNumber", is("A")))
                .andExpect(jsonPath("$.sideNumber", is("AA")))
                .andExpect(jsonPath("$.color", is("AAA")))
                .andExpect(jsonPath("$.type", is("AAAA")))
                .andExpect(jsonPath("$.rangeKm", is(10)))
                .andExpect(jsonPath("$.batteryLevelPct", is(10)))
                .andExpect(jsonPath("$.status", is("a")))
                .andExpect(jsonPath("$.address", is("aa")))
                .andExpect(jsonPath("$.name", is("aaa")))
                .andExpect(jsonPath("$.location.latitude", is(51.0)))
                .andExpect(jsonPath("$.location.longitude", is(16.0)));
    }

    @Test
    void shouldFetchCarConsumesLocation() throws Exception {
        //Given
        when(service.getNearestAvailableCar(any(GPSLocationDtoNoIdNoType.class))).thenReturn(new VozillaCarDto(
                "A", "AA", "AAA", "AAAA", 10, 10,
                "a", "aa", "aaa",
                new GPSLocation(51.0, 16.0)));
        Gson gson = new Gson();
        String jsonString = gson.toJson(new GPSLocationDtoNoIdNoType(51.5, 16.5));

        //When & Then
        mockMvc.perform(get("/cars/location")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(jsonString))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.platesNumber", is("A")))
                .andExpect(jsonPath("$.sideNumber", is("AA")))
                .andExpect(jsonPath("$.color", is("AAA")))
                .andExpect(jsonPath("$.type", is("AAAA")))
                .andExpect(jsonPath("$.rangeKm", is(10)))
                .andExpect(jsonPath("$.batteryLevelPct", is(10)))
                .andExpect(jsonPath("$.status", is("a")))
                .andExpect(jsonPath("$.address", is("aa")))
                .andExpect(jsonPath("$.name", is("aaa")))
                .andExpect(jsonPath("$.location.latitude", is(51.0)))
                .andExpect(jsonPath("$.location.longitude", is(16.0)));

    }

    @Test
    void shouldHandleUserNotFoundException() throws Exception {
        //Given
        when(service.getNearestAvailableCar(anyLong())).thenThrow(new UserNotFoundException());

        //When & Then
        mockMvc.perform(get("/cars/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(status().reason("No user with given id"));
    }

    @Test
    void shouldFetchCarConsumesUserId() throws Exception {
        //Given
        when(service.getNearestAvailableCar(anyLong())).thenReturn(new VozillaCarDto(
                "A", "AA", "AAA", "AAAA", 10, 10,
                "a", "aa", "aaa",
                new GPSLocation(51.0, 16.0)));

        //When & Then
        mockMvc.perform(get("/cars/user/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.platesNumber", is("A")))
                .andExpect(jsonPath("$.sideNumber", is("AA")))
                .andExpect(jsonPath("$.color", is("AAA")))
                .andExpect(jsonPath("$.type", is("AAAA")))
                .andExpect(jsonPath("$.rangeKm", is(10)))
                .andExpect(jsonPath("$.batteryLevelPct", is(10)))
                .andExpect(jsonPath("$.status", is("a")))
                .andExpect(jsonPath("$.address", is("aa")))
                .andExpect(jsonPath("$.name", is("aaa")))
                .andExpect(jsonPath("$.location.latitude", is(51.0)))
                .andExpect(jsonPath("$.location.longitude", is(16.0)));

    }

    private List<VozillaCarDto> prepareCarList() {
        VozillaCarDto car1 = new VozillaCarDto(
                "A", "AA", "AAA", "AAAA", 10, 10,
                "a", "aa", "aaa",
                new GPSLocation(51.0, 16.0));
        VozillaCarDto car2 = new VozillaCarDto(
                "B", "BB", "BBB", "BBBB", 20, 20,
                "b", "bb", "bbb",
                new GPSLocation(51.5, 16.5));
        VozillaCarDto car3 = new VozillaCarDto(
                "C", "CC", "CCC", "CCCC", 30, 30,
                "c", "cc", "ccc",
                new GPSLocation(52.0, 17.0));
        return new ArrayList<>(Arrays.asList(car1, car2, car3));
    }
}
