package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.weather.WeatherStation;
import com.wroclawhelperb.domain.weather.WeatherStationDto;
import com.wroclawhelperb.exception.NoStationIdInMapException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
import com.wroclawhelperb.mapper.WeatherStationMapper;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.wroclawhelperb.domain.location.GPSLocation.WEATHER_STATION_LOCATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class WeatherStationServiceTestSuite {

    private static final String TEST_SHORT_NAME = "Test Short Name";
    private static final String TEST_NAME = "Test LAst Name";
    private static final GPSLocation LOCATION = new GPSLocation(1.0, 1.5, WEATHER_STATION_LOCATION);

    @Autowired
    private WeatherStationService service;

    @MockBean
    private WeatherStationMapper mapper;

    @MockBean
    private WeatherStationRepository repository;

    @Test
    void shouldGetWeatherStationsReturnStationList() {
        //Given

        WeatherStation station = new WeatherStation(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        WeatherStation station1 = new WeatherStation("SN1", "N1",
                new GPSLocation(2.0, 2.5, WEATHER_STATION_LOCATION));
        WeatherStation station2 = new WeatherStation("SN2", "N2",
                new GPSLocation(3.0, 3.5, WEATHER_STATION_LOCATION));
        List<WeatherStation> stations = new ArrayList<>(Arrays.asList(station, station1, station2));

        WeatherStationDto stationDto = new WeatherStationDto(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        WeatherStationDto station1Dto = new WeatherStationDto("SN1", "N1",
                new GPSLocation(2.0, 2.5, WEATHER_STATION_LOCATION));
        WeatherStationDto station2Dto = new WeatherStationDto("SN2", "N2",
                new GPSLocation(3.0, 3.5, WEATHER_STATION_LOCATION));

        List<WeatherStationDto> stationsDto = new ArrayList<>(Arrays.asList(stationDto, station1Dto, station2Dto));

        when(repository.findAll()).thenReturn(stations);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(stationDto);
        when(mapper.mapToWeatherStationDto(station1)).thenReturn(station1Dto);
        when(mapper.mapToWeatherStationDto(station2)).thenReturn(station2Dto);

        //When
        List<WeatherStationDto> returnedList = service.getWeatherStations();

        //Then
        assertEquals(stationsDto, returnedList);
    }

    @Test
    void shouldGetWeatherStationsFilterUnknownAndFetchEmptyList() {
        //Given
        WeatherStation station = new WeatherStation("UNKNOWN", TEST_NAME, LOCATION);
        List<WeatherStation> stations = new ArrayList<>();
        stations.add(station);
        WeatherStationDto stationDto = new WeatherStationDto("UNKNOWN", TEST_NAME, LOCATION);
        when(repository.findAll()).thenReturn(stations);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(stationDto);

        //When
        List<WeatherStationDto> returnedList = service.getWeatherStations();

        //Then
        assertEquals(new ArrayList<>(), returnedList);
    }

    @Test
    void shouldGetWeatherStationsFetchEmptyList() {
        //Given
        when(repository.findAll()).thenReturn(new ArrayList<>());

        //When
        List<WeatherStationDto> returnedList = service.getWeatherStations();

        //Then
        assertEquals(new ArrayList<>(), returnedList);
    }

    @Test
    void shouldAddNewWeatherStationReturnSavedStation() {
        //Given
        WeatherStation station = new WeatherStation(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        WeatherStationDto stationDto = new WeatherStationDto(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(stationDto);
        when(mapper.mapToWeatherStation(stationDto)).thenReturn(station);
        when(repository.save(station)).thenReturn(station);

        //When
        WeatherStationDto returnedStation = service.addNewWeatherStation(stationDto);

        //Then
        assertEquals(stationDto, returnedStation);
    }

    @Test
    void shouldUpdateStationReturnUpdatedStation() throws WeatherStationNotFoundException {
        //Given
        WeatherStation stationToUpdate = new WeatherStation(TEST_SHORT_NAME, "", new GPSLocation());
        WeatherStation station = new WeatherStation(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        WeatherStationDto stationDto = new WeatherStationDto(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        when(repository.findById(anyString())).thenReturn(Optional.of(stationToUpdate));
        when(mapper.mapToWeatherStationDto(station)).thenReturn(stationDto);
        when(repository.save(any(WeatherStation.class))).thenReturn(station);

        //When
        WeatherStationDto returnedStation = service.updateStation(stationDto);

        //Then
        assertEquals(stationDto, returnedStation);
    }

    @Test
    void shouldUpdateStationThrowWeatherStationNotFoundException() {
        //Given
        WeatherStationDto stationDto = new WeatherStationDto(TEST_SHORT_NAME, TEST_NAME, LOCATION);
        when(repository.findById(anyString())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(WeatherStationNotFoundException.class, () -> service.updateStation(stationDto));
    }

    @Test
    void shouldUpdateStationPropertyThrowNoStationIdInMapException() {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("someKey", "someValue");
        map.put("someOtherKey", "someOtherValue");

        //When & Then
        assertThrows(NoStationIdInMapException.class, () -> service.updateStationProperty(map));
    }

    @Test
    void shouldUpdateStationPropertyThrowWeatherStationNotFoundException() {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(repository.findById("someValue")).thenReturn(Optional.empty());

        //When & Then
        assertThrows(WeatherStationNotFoundException.class, () -> service.updateStationProperty(map));
    }

    @Test
    void shouldUpdateStationPropertyNameReturnUpdatedStation()
            throws WeatherStationNotFoundException, NoStationIdInMapException {
        //Given
        WeatherStation stationToUpdate = new WeatherStation("someValue", "", new GPSLocation());
        when(repository.findById("someValue")).thenReturn(Optional.of(stationToUpdate));
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("name", "updatedName");
        WeatherStation station = new WeatherStation("someValue", "updatedName", LOCATION);
        WeatherStationDto updatedStation = new WeatherStationDto("someValue", "updatedName", LOCATION);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(updatedStation);
        when(repository.save(any(WeatherStation.class))).thenReturn(station);

        //When
        WeatherStationDto returnedStation = service.updateStationProperty(map);

        //Then
        assertEquals(updatedStation, returnedStation);
    }

    @Test
    void shouldUpdateStationPropertyLatitudeReturnUpdatedStation()
            throws WeatherStationNotFoundException, NoStationIdInMapException {
        //Given
        GPSLocation updatedLocation = new GPSLocation( 53.5, 1.5, WEATHER_STATION_LOCATION);
        WeatherStation stationToUpdate = new WeatherStation("someValue", "", new GPSLocation());
        when(repository.findById("someValue")).thenReturn(Optional.of(stationToUpdate));
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("latitude", "53.5");
        WeatherStation station = new WeatherStation("someValue", "", updatedLocation);
        WeatherStationDto updatedStation = new WeatherStationDto("someValue", "", updatedLocation);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(updatedStation);
        when(repository.save(any(WeatherStation.class))).thenReturn(station);

        //When
        WeatherStationDto returnedStation = service.updateStationProperty(map);

        //Then
        assertEquals(updatedStation, returnedStation);
    }
    @Test
    void shouldUpdateStationPropertyLongitudeReturnUpdatedStation()
            throws WeatherStationNotFoundException, NoStationIdInMapException {
        //Given
        GPSLocation updatedLocation = new GPSLocation( 1.0, 17.5, WEATHER_STATION_LOCATION);
        WeatherStation stationToUpdate = new WeatherStation("someValue", "", new GPSLocation());
        when(repository.findById("someValue")).thenReturn(Optional.of(stationToUpdate));
        Map<String, String> map = new HashMap<>();
        map.put("shortName", "someValue");
        map.put("longitude", "17.5");
        WeatherStation station = new WeatherStation("someValue", "", updatedLocation);
        WeatherStationDto updatedStation = new WeatherStationDto("someValue", "", updatedLocation);
        when(mapper.mapToWeatherStationDto(station)).thenReturn(updatedStation);
        when(repository.save(any(WeatherStation.class))).thenReturn(station);

        //When
        WeatherStationDto returnedStation = service.updateStationProperty(map);

        //Then
        assertEquals(updatedStation, returnedStation);
    }

}
