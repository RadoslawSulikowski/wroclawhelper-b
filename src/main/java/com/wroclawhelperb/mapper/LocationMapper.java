package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public GPSLocationDtoNoIdNoType mapToLocationDtoNoIdNoType(GPSLocation location) {
        return new GPSLocationDtoNoIdNoType(
                location.getLatitude(),
                location.getLongitude()
        );
    }

    public GPSLocation mapToLocation(GPSLocationDtoNoIdNoType location) {
        return new GPSLocation(
                location.getLatitude(),
                location.getLongitude()
        );
    }
}
