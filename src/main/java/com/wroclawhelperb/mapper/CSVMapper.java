package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.wroclawhelperb.domain.weather.WeatherStation.nameToShortName;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

@Component
public class CSVMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVMapper.class);

    public List<WeatherDtoNoId> mapToWeatherList(URL url) {
        try {
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");

            List<WeatherDtoNoId> weatherList = new ArrayList<>();
            int lineCounter = 0;
            while (scanner.hasNext()) {
                String[] line = scanner.nextLine().split(",");
                if (lineCounter > 1) {
                    weatherList.add(new WeatherDtoNoId(
                            LocalDateTime.parse(line[1].substring(0, 10) + "T" + line[1].substring(11, 16)),
                            parseDouble(line[2]),
                            parseDouble(line[3]),
                            parseDouble(line[4]),
                            parseDouble(line[5]),
                            parseDouble(line[6]),
                            line[7],
                            nameToShortName(line[8])));
                }
                lineCounter++;
            }
            scanner.close();
            return weatherList;
        } catch(IOException e) {
            LOGGER.error("Can not obtain data from external Weather API");
            return new ArrayList<>();
        }
    }

    public List<WeatherDtoNoId> mapToWeatherList(String source) {
        try {
            URL url = new URL(source);
            return mapToWeatherList(url);
        } catch(MalformedURLException e) {
            LOGGER.error("Can not creat URL from given String");
            return new ArrayList<>();
        }
    }

    public List<BikeStationDto> mapToBikeStationList(URL url) {
        try {
            Scanner scanner = new Scanner(url.openStream(), "UTF-8");
            List<BikeStationDto> bikeStationList = new ArrayList<>();
            int lineCounter = 0;
            while (scanner.hasNext()) {

                String line = scanner.nextLine();

                if (lineCounter > 0) {
                    List<Integer> bikeList = new ArrayList<>();
                    String[] lineArr, line0Arr, line1Arr, bikesArr;

                    if (line.contains("[{")) {
                        lineArr = line.split(",\"\\[|]\",");
                        line0Arr = lineArr[0].split(",");
                        bikesArr = lineArr[1].split("\"\": \"\"|\"\", \"\"");
                        line1Arr = lineArr[2].split(",");
                        for (int i = 0; i < bikesArr.length; i++) {
                            if (i % 2 == 1) {
                                bikeList.add(parseInt(bikesArr[i]));
                            }
                        }
                    } else {
                        lineArr = line.split(",,");
                        line0Arr = lineArr[0].split(",");
                        line1Arr = lineArr[1].split(",");
                    }

                    bikeStationList.add(new BikeStationDto(
                            parseLong(line0Arr[1]), //Long uniqueId;
                            parseInt(line0Arr[2]), //int number;
                            parseInt(line0Arr[3]), //int bookedBikes;
                            parseInt(line0Arr[4]), //int bikes;
                            bikeList, //List<Integer> bikeList;
                            new GPSLocation(parseDouble(line1Arr[0]), parseDouble(line1Arr[1])), //GPSLocation location; //lat,lng,
                            line1Arr[2] //String name;
                    ));
                }
                lineCounter++;
            }
            scanner.close();
            return bikeStationList;
        } catch(IOException e) {
            LOGGER.error("Can not obtain data from external Bike Station API");
            return new ArrayList<>();
        }
    }

    public List<BikeStationDto> mapToBikeStationList(String source) {
        try {
            URL url = new URL(source);
            return mapToBikeStationList(url);
        } catch(MalformedURLException e) {
            LOGGER.error("Can not creat URL from given String");
            return new ArrayList<>();
        }
    }
}
