package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.weather.WeatherStation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.wroclawhelperb.domain.weather.WeatherStation.nameToShortName;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class WeatherStationRepositoryTestSuite {

    @Autowired
    private WeatherStationRepository repository;

    @Test
    public void testSaveWeatherStation() {
        //Given
        WeatherStation station = new WeatherStation(nameToShortName("test"), "test long name",
                new GPSLocation(51.131199, 16.984879, GPSLocation.WEATHER_STATION_LOCATION));
//        WeatherStation station = new WeatherStation(nameToShortName(MILENIJNY), MILENIJNY,
//                new GPSLocation(51.131199, 16.984879, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station1 = new WeatherStation(nameToShortName(WARSZAWSKI), WARSZAWSKI,
//                new GPSLocation(51.128788, 17.056380, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station2 = new WeatherStation(nameToShortName(DMOWSKIEGO), DMOWSKIEGO,
//                new GPSLocation(51.117541, 17.017372, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station3 = new WeatherStation(nameToShortName(SOBIESKIEGO), SOBIESKIEGO,
//                new GPSLocation(51.156316, 17.137665, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station4 = new WeatherStation(nameToShortName(LOTNICZA), LOTNICZA,
//                new GPSLocation(51.137982, 16.945764, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station5 = new WeatherStation(nameToShortName(GADOWIANKA), GADOWIANKA,
//                new GPSLocation(51.112853, 16.968712, GPSLocation.WEATHER_STATION_LOCATION));
//
//        WeatherStation station6 = new WeatherStation(nameToShortName(OPOLSKA), OPOLSKA,
//                new GPSLocation(51.076724, 17.084888, GPSLocation.WEATHER_STATION_LOCATION));

        //When
        station = repository.save(station);
        String stationId = station.getShortName();

        //Then
        assertTrue(repository.findById(stationId).isPresent());

        //Clean Up
        repository.deleteById(stationId);
    }
}
