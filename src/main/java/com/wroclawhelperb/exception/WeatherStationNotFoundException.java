package com.wroclawhelperb.exception;

public class WeatherStationNotFoundException extends Exception {

    public WeatherStationNotFoundException() {
        super();
    }

    public WeatherStationNotFoundException(String message) {
        super(message);
    }
}
