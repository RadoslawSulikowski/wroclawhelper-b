package com.wroclawhelperb.domain;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.weather.WeatherStation;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static com.wroclawhelperb.domain.weather.WeatherStation.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestSuite {

    @Test
    public void testFindNearest() {
        //Given
        List<WeatherStation> stations = new ArrayList<>();
        stations.add(new WeatherStation(nameToShortName(MILENIJNY), MILENIJNY,
                new GPSLocation(51.131199, 16.984879, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(WARSZAWSKI), WARSZAWSKI,
                new GPSLocation(51.128788, 17.056380, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(DMOWSKIEGO), DMOWSKIEGO,
                new GPSLocation(51.117541, 17.017372, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(SOBIESKIEGO), SOBIESKIEGO,
                new GPSLocation(51.156316, 17.137665, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(LOTNICZA), LOTNICZA,
                new GPSLocation(51.137982, 16.945764, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(GADOWIANKA), GADOWIANKA,
                new GPSLocation(51.112853, 16.968712, GPSLocation.WEATHER_STATION_LOCATION)));
        stations.add(new WeatherStation(nameToShortName(OPOLSKA), OPOLSKA,
                new GPSLocation(51.076724, 17.084888, GPSLocation.WEATHER_STATION_LOCATION)));
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(51.138235, 16.973045, GPSLocation.USER_FAVORITE_LOCATION), true);

        //When
        WeatherStation nearestStation = (WeatherStation) user.getLocation().findNearest(stations);

        //Then
        assertEquals(MILENIJNY, nearestStation.getName());
    }
}
