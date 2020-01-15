package com.wroclawhelperb.domain;

import com.wroclawhelperb.domain.user.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestSuite {

    @Test
    public void testFindNearest() {
        //Given
        List<Locable> stations = new ArrayList<>();
        stations.add(new WeatherStation(WeatherStation.MILENIJNY,
                new GPSLocation(51.131199, 16.984879, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.WARSZAWSKI,
                new GPSLocation(51.128788, 17.056380, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.DMOWSKIEGO,
                new GPSLocation(51.117541, 17.017372, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.SOBIESKIEGO,
                new GPSLocation(51.156316, 17.137665, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.LOTNICZA,
                new GPSLocation(51.137982, 16.945764, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.GADOWIANKA,
                new GPSLocation(51.112853, 16.968712, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(WeatherStation.OPOLSKA,
                new GPSLocation(51.076724, 17.084888, GPSLocation.WEATHER_STATION_LOCATION)));
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(51.138235, 16.973045, GPSLocation.USER_FAVORITE_LOCATION));

        //When
        WeatherStation nearestStation = (WeatherStation) user.findNearest(stations);

        //Then
        assertEquals(WeatherStation.MILENIJNY, nearestStation.getName());
    }
}
