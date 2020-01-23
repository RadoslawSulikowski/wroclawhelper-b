package com.wroclawhelperb.domain.bike.station;

import com.wroclawhelperb.domain.location.GPSLocationDtoNoIdNoType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeStationDto {

    Long sourceId;
    Long uniqueId;
    int number;
    int bookedBikes;
    int bikes;
    List<Integer> bikeList;
    GPSLocationDtoNoIdNoType location;
    String name;

    @Override
    public String toString() {
        return "BikeStationDto{" +
                "sourceId=" + sourceId +
                ", uniqueId=" + uniqueId +
                ", number=" + number +
                ", bookedBikes=" + bookedBikes +
                ", bikes=" + bikes +
                ", bikeList=" + bikeList +
                ", location=" + location +
                ", name='" + name + '\'' +
                '}';
    }
}
