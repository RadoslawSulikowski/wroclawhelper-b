package com.wroclawhelperb.domain.bike;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BikeStationDto implements Locable {

    Long uniqueId;
    int number;
    int bookedBikes;
    int bikes;
    List<Integer> bikeList;
    GPSLocation location;
    String name;

    @Override
    public String toString() {
        return "BikeStationDto{" +
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
