package com.wroclawhelperb.controller;


import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.exception.WeatherStationNotFoundException;
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
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No station with given id")
    public String weatherStationNotFoundExceptionHandler() {
        LOGGER.error("No such Weather Station");
        return "No such Weather Station";
    }

}
