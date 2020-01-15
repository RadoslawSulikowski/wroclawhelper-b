package com.wroclawhelperb.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationTestSuite {

    @Test
    public void testGetDistance() {
        //Given
        GPSLocation loc1 = new GPSLocation(51.130834, 16.977835, GPSLocation.USER_FAVORITE_LOCATION);
        GPSLocation loc2 = new GPSLocation(51.134791, 16.976905, GPSLocation.USER_FAVORITE_LOCATION);
        GPSLocation loc3 = new GPSLocation(51.143134, 17.024957, GPSLocation.USER_FAVORITE_LOCATION);

        //When
        double distance1 = loc1.getDistance(loc2);
        double distance2 = loc1.getDistance(loc3);

        //Then
        assertEquals(445, distance1, 0);
        assertEquals(3560, distance2, 1);
    }
}
