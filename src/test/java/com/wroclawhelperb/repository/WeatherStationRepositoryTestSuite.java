package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.GPSLocation;
import com.wroclawhelperb.domain.WeatherStation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WeatherStationRepositoryTestSuite {

    @Autowired
    private WeatherStationRepository repository;

    @Test
    public void testSaveWeatherStation() {
        //Given
        WeatherStation station = new WeatherStation(WeatherStation.MILENIJNY,
                new GPSLocation(51.131199, 16.984879, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station1 = new WeatherStation(WeatherStation.WARSZAWSKI,
                new GPSLocation(51.128788, 17.056380, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station2 = new WeatherStation(WeatherStation.DMOWSKIEGO,
                new GPSLocation(51.117541, 17.017372, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station3 = new WeatherStation(WeatherStation.SOBIESKIEGO,
                new GPSLocation(51.156316, 17.137665, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station4 = new WeatherStation(WeatherStation.LOTNICZA,
                new GPSLocation(51.137982, 16.945764, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station5 = new WeatherStation(WeatherStation.GADOWIANKA,
                new GPSLocation(51.112853, 16.968712, GPSLocation.WEATHER_STATION_LOCATION));

        WeatherStation station6 = new WeatherStation(WeatherStation.OPOLSKA,
                new GPSLocation(51.076724, 17.084888, GPSLocation.WEATHER_STATION_LOCATION));

        //When
        station = repository.save(station);
        Long stationId = station.getId();

        //Then
        assertTrue(repository.findById(stationId).isPresent());

        //Clean Up
        repository.deleteById(stationId);
    }
}
