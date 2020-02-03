package com.wroclawhelperb.service.mail;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.BikeService;
import com.wroclawhelperb.service.VozillaService;
import com.wroclawhelperb.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class EmailSchedulerTestSuite {

    @Autowired
    private EmailScheduler scheduler;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private VozillaService carService;

    @MockBean
    private BikeService bikeService;

    private static final LocalDateTime MEASURING_TIME
            = LocalDateTime.of(2020, 2, 2, 2, 22, 22);
    private static final double HARD_WIND_SPEED = 9.1;
    private static final double LIGHT_WIND_SPEED = 2.4;
    private static final double WIND_DIRECTION = 22.5;
    private static final double HUMIDITY = 74.4;
    private static final double AIR_TEMPERATURE_FREEZE = -11.1;
    private static final double AIR_TEMPERATURE_NO_FREEZE = 2.4;
    private static final double GROUND_TEMPERATURE = 12.4;
    private static final String PRECIPITATION_TYPE_CONTINUOUS = "Opad ciągły";
    private static final String PRECIPITATION_TYPE_NONE = "Brak opadu";
    private static final String WEATHER_STATION_NAME = "Weather station name";
    private static final String BIKE_STATION_NAME = "Bike station name";

    @Test
    void shouldWhatToGoThrowUserNotFoundExceptionByCarService() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, WEATHER_STATION_NAME);
        when(carService.getNearestAvailableCar(anyLong())).thenThrow(new UserNotFoundException());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> scheduler.whatToGo(user, weather));
    }

    @Test
    void shouldWhatToGoThrowUserNotFoundExceptionByBikeService() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, WEATHER_STATION_NAME);
        when(bikeService.getNearestStationWithAvailableBike(anyLong())).thenThrow(new UserNotFoundException());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> scheduler.whatToGo(user, weather));
    }

    @Test
    void shouldWhatToGoChooseCar() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        VozillaCarDto car = new VozillaCarDto("A", "AA", "AAA", "AAAA", 10,
                10, "a", "aa", "aaa", new GPSLocation(51.0, 16.0));
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, WEATHER_STATION_NAME);
        when(carService.getNearestAvailableCar(anyLong())).thenReturn(car);
        String expectedMessage = "\tToday is windy, is precipitation, is frost, so you should go by car.\n" +
                "The nearest available Vozilla car(AAA aaa A) from set location you can find on:\n" +
                "aa under coordinates: 51.0 16.0";

        //When
        String message = scheduler.whatToGo(user, weather);

        //Then
        assertEquals(expectedMessage, message);
    }

    @Test
    void shouldWhatToGoChooseBike() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        BikeStationDto bikeStation = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), BIKE_STATION_NAME);
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, WEATHER_STATION_NAME);
        when(bikeService.getNearestStationWithAvailableBike(anyLong())).thenReturn(bikeStation);
        String expectedMessage = "\tToday is nice weather, so you should consider going by bike!\n\n" +
                "The nearest WRM bike station with available 2 bikes:\nBike station name under coordinates: 1.0 1.5";

        //When
        String message = scheduler.whatToGo(user, weather);

        //Then
        assertEquals(expectedMessage, message);
    }

    @Test
    void shouldPrepareMailThrowUserNotFoundException() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        when(weatherService.getWeatherOnNearestStation(anyLong())).thenThrow(new UserNotFoundException());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> scheduler.prepareMail(user));
    }

    @Test
    void shouldPrepareMailReturnMailAndProposeCar() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setEmail("user@emial.com");
        VozillaCarDto car = new VozillaCarDto("A", "AA", "AAA", "AAAA", 10,
                10, "a", "aa", "aaa", new GPSLocation(51.0, 16.0));
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, HARD_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_CONTINUOUS, WEATHER_STATION_NAME);
        when(carService.getNearestAvailableCar(anyLong())).thenReturn(car);
        when(weatherService.getWeatherOnNearestStation(anyLong())).thenReturn(weather);
        String expectedMessage = "\tGood morning, First Name!\n\nAre you wondering what the weather is like outside " +
                "the window?\nLuckily your scheduler on wroclawhelper is ON!\n\nThe nearest Weather Station from " +
                "location which your set in your profile is on:\nWeather station name\n\nThe weather was updated " +
                "2020-02-02 02:22:22\nWind:\t\t\t\t\t   9.1m/s N\nAir temperature:\t\t\t-11.1\u00B0C\nGround " +
                "temperature:\t\t  12.4\u00B0C\nPrecipitation type:\t\t\tCONTINUOUS\nAir humidity:\t\t\t\t  " +
                "74.4%\n\n\tToday is windy, is precipitation, is frost, so you should go by car.\n" +
                "The nearest available Vozilla car(AAA aaa A) from set location you can find on:\n" +
                "aa under coordinates: 51.0 16.0\n\nHave a nice day!\n\t\tyour woclawhelper!";

        //When
        Mail mail = scheduler.prepareMail(user);

        //Then
        assertEquals(expectedMessage, mail.getMessage());
        assertEquals("user@emial.com", mail.getSendTo());
        assertEquals("What's the weather like today in Wroclaw", mail.getSubject());
    }

    @Test
    void shouldPrepareMailReturnMailAndProposeBike() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        user.setFirstName("First Name");
        user.setEmail("user@emial.com");
        BikeStationDto bikeStation = new BikeStationDto(1L, 1, 1, 3,
                new ArrayList<>(Arrays.asList(123, 456, 789)),
                new GPSLocation(1.0, 1.5), BIKE_STATION_NAME);
        WeatherDtoNoId weather = new WeatherDtoNoId(MEASURING_TIME, LIGHT_WIND_SPEED, WIND_DIRECTION, HUMIDITY,
                AIR_TEMPERATURE_NO_FREEZE, GROUND_TEMPERATURE, PRECIPITATION_TYPE_NONE, WEATHER_STATION_NAME);
        when(bikeService.getNearestStationWithAvailableBike(anyLong())).thenReturn(bikeStation);
        when(weatherService.getWeatherOnNearestStation(anyLong())).thenReturn(weather);
        String expectedMessage = "\tGood morning, First Name!\n\nAre you wondering what the weather is like outside " +
                "the window?\nLuckily your scheduler on wroclawhelper is ON!\n\nThe nearest Weather Station from " +
                "location which your set in your profile is on:\nWeather station name\n\nThe weather was updated " +
                "2020-02-02 02:22:22\nWind:\t\t\t\t\t   2.4m/s N\nAir temperature:\t\t\t2.4\u00B0C\nGround " +
                "temperature:\t\t  12.4\u00B0C\nPrecipitation type:\t\t\tNONE\nAir humidity:\t\t\t\t  " +
                "74.4%\n\n\tToday is nice weather, so you should consider going by bike!\n\n" +
                "The nearest WRM bike station with available 2 bikes:\nBike station name under coordinates: 1.0 1.5" +
                "\n\nHave a nice day!\n\t\tyour woclawhelper!";

        //When
        Mail mail = scheduler.prepareMail(user);

        //Then
        assertEquals(expectedMessage, mail.getMessage());
        assertEquals("user@emial.com", mail.getSendTo());
        assertEquals("What's the weather like today in Wroclaw", mail.getSubject());
    }
}
