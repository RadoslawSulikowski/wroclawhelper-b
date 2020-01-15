package com.wroclawhelperb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static java.lang.Math.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "Locations")
@Table(name = "locations")
public class GPSLocation {

    public static final String USER_FAVORITE_LOCATION = "USER_FAVORITE_LOCATION";
    public static final String WEATHER_STATION_LOCATION = "WEATHER_STATION_LOCATION";


    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    public GPSLocation(double latitude, double longitude, String locationType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationType = locationType;
    }

    public double getDistance(GPSLocation location) {
        return round(acos(sin(toRadians(latitude)) * sin(toRadians(location.getLatitude()))
                + cos(toRadians(latitude)) * cos(toRadians(location.getLatitude()))
                * cos(toRadians(location.getLongitude() - longitude))) * 6371000);
    }
}
