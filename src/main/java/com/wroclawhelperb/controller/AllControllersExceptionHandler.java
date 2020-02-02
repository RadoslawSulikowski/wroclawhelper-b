package com.wroclawhelperb.controller;


import com.wroclawhelperb.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AllControllersExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllControllersExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No user with given id")
    public String userNotFoundExceptionHandler() {
        LOGGER.error("No such user.");
        return "No such user.";
    }

    @ExceptionHandler(WeatherStationNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Weather Station with given short name")
    public String weatherStationNotFoundExceptionHandler() {
        LOGGER.error("No such Weather Station");
        return "No such Weather Station";
    }

    @ExceptionHandler(BikeStationNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Bike Station with given id")
    public String bikeStationNotFoundExceptionHandler() {
        LOGGER.error("No such Bike Station");
        return "No such Bike Station";
    }

    @ExceptionHandler(CarNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No car with given plates number")
    public String carNotFoundExceptionHandler() {
        LOGGER.error("No car with given plates number");
        return "No car with given plates number";
    }

    @ExceptionHandler(NoUsernameInMapException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Username not found in received data")
    public String noUsernameInMapExceptionHandler() {
        LOGGER.error("Username not found in received data");
        return "Username not found in received data";
    }
}
