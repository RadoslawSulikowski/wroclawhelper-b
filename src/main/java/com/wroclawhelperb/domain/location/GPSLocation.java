package com.wroclawhelperb.domain.location;

import com.wroclawhelperb.domain.Locable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    public GPSLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getDistance(GPSLocation location) {
        return round(acos(sin(toRadians(latitude)) * sin(toRadians(location.getLatitude()))
                + cos(toRadians(latitude)) * cos(toRadians(location.getLatitude()))
                * cos(toRadians(location.getLongitude() - longitude))) * 6371000);
    }


    public Locable findNearest(List<? extends Locable> locableList) {
        if (locableList != null && locableList.size() > 0) {
            Locable locable = locableList.get(0);
            for (Locable l : locableList) {
                if (this.getDistance(l.getLocation()) < this.getDistance(locable.getLocation())) {
                    locable = l;
                }
            }
            return locable;
        }
        //throw some Exception?
        return null;
    }

    @Override
    public String toString() {
        return "GPSLocation{" +
                "id=" + id +
                ", locationType='" + locationType + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}

