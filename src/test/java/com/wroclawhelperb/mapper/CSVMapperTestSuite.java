package com.wroclawhelperb.mapper;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CSVMapperTestSuite {

    @Autowired
    private CSVMapper csvMapper;

    private List<ILoggingEvent> prepareLogList() {
        Logger csvMapperLogger = (Logger) LoggerFactory.getLogger(CSVMapper.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        csvMapperLogger.addAppender(listAppender);
        return listAppender.list;
    }

    @Test
    void shouldLogErrorMalformedURLExceptionAndReturnEmptyArray() {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        String sUrl = "wrong string url address";

        //When
        List<WeatherDtoNoId> list = csvMapper.mapToWeatherList(sUrl);

        //Then
        assertTrue(list instanceof ArrayList);
        assertEquals(0, list.size());
        assertThat(logsList).extracting("message")
                .containsOnly("Can not creat URL from given String");
    }

    @Test
    void shouldLogErrorIOExceptionAndReturnEmptyArray() throws MalformedURLException {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        URL url = new URL("https://www.wroclaw.pl/open-data/datastore/dump/9d5b2336");

        //When
        List<WeatherDtoNoId> list = csvMapper.mapToWeatherList(url);

        //Then
        assertTrue(list instanceof ArrayList);
        assertEquals(0, list.size());
        assertThat(logsList).extracting("message")
                .containsOnly("Can not obtain data from external Weather API");
    }

    @Test
    void shouldFetchBikeStationList() throws MalformedURLException {
        URL url = new URL("https://www.wroclaw.pl/open-data/datastore/dump/42eea6ec-43c3-4d13-aa77-a93394d6165a");

        List<BikeStationDto> bikeStationList = csvMapper.mapToBikeStationList(url);

        bikeStationList.forEach(System.out::println);
    }
}
