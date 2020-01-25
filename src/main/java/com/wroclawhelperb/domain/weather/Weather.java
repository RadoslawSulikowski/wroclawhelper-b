package com.wroclawhelperb.domain.weather;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity(name = "Weather")
@Table(name = "weather")
public class Weather implements Locable {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "measuringTime")
    private LocalDateTime measuringTime;

    @Column(name = "windSpeed")
    private double windSpeed;

    @Column(name = "windDirection")
    private double windDirection;

    @Column(name = "humidity")
    private double humidity;

    @Column(name = "airTemperature")
    private double airTemperature;

    @Column(name = "groundTemperature")
    private double groundTemperature;

    @Column(name = "precipitationType")
    private String precipitationType;

    @ManyToOne(optional = false, targetEntity = WeatherStation.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "weatherStationName", referencedColumnName = "shortName")
    private WeatherStation weatherStation;

    public Weather(LocalDateTime measuringTime, double windSpeed, double windDirection,
                   double humidity, double airTemperature, double groundTemperature,
                   String precipitationType, WeatherStation weatherStation) {
        this.measuringTime = measuringTime;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.humidity = humidity;
        this.airTemperature = airTemperature;
        this.groundTemperature = groundTemperature;
        this.precipitationType = precipitationType;
        this.weatherStation = weatherStation;
    }

    public GPSLocation getLocation() {
        return weatherStation.getLocation();
    }

    @Override
    public String toString() {
        return "Weather{" +
                "id=" + id +
                ", measuringTime=" + measuringTime +
                ", windSpeed=" + windSpeed +
                ", windDirection=" + windDirection +
                ", humidity=" + humidity +
                ", airTemperature=" + airTemperature +
                ", groundTemperature=" + groundTemperature +
                ", precipitationType='" + precipitationType + '\'' +
                ", weatherStation=" + weatherStation +
                '}';
    }
}
