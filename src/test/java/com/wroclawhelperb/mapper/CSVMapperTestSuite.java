package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.logging.StaticAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.wroclawhelperb.mapper.CSVMapper.mapToWeatherList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CSVMapperTestSuite {

    @BeforeEach
    public void clearLoggingStatements() {
        StaticAppender.clearEvents();
    }

    @Test
    public void shouldLogErrorMalformedURLExceptionAndReturnEmptyArray() {
        //Given
        String sUrl = "wrong string url address";

        //When
        List<WeatherDtoNoId> list = mapToWeatherList(sUrl);

        //Then
        assertTrue(list instanceof ArrayList);
        assertEquals(0, list.size());
        assertThat(StaticAppender.getEvents()).extracting("message")
                .containsOnly("Can not creat URL from given String");
    }

    @Test
    public void shouldLogErrorIOExceptionAndReturnEmptyArray() throws MalformedURLException {
        //Given
        URL url = new URL("https://www.wroclaw.pl/open-data/datastore/dump/9d5b2336");

        //When
        List<WeatherDtoNoId> list = mapToWeatherList(url);

        //Then
        assertTrue(list instanceof ArrayList);
        assertEquals(0, list.size());
        assertThat(StaticAppender.getEvents()).extracting("message")
                .containsOnly("Can not obtain data from external Weather API");
    }
}
