package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.weather.WeatherDtoNoId;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static com.wroclawhelperb.domain.weather.WeatherStation.nameToShortName;
import static java.lang.Double.parseDouble;

public class CSVMapper {

    public static List<WeatherDtoNoId> mapToWeatherList(URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream(), "UTF-8");
        List<WeatherDtoNoId> weatherList = new ArrayList<>();
        int lineCounter = 0;
        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(",");
            if (lineCounter > 1) {
                weatherList.add(new WeatherDtoNoId(
                        Long.parseLong(line[0]),
                        LocalDateTime.parse(line[1].substring(0, 10) + "T" + line[1].substring(11, 16)),
                        parseDouble(line[2]),
                        parseDouble(line[3]),
                        parseDouble(line[4]),
                        parseDouble(line[5]),
                        parseDouble(line[6]),
                        line[7],
                        nameToShortName(line[8])
                ));
            }
            lineCounter++;
        }
        scanner.close();
        return weatherList;
    }

    public static List<WeatherDtoNoId> mapToWeatherList(String source) throws IOException {
        URL url = new URL(source);
        return mapToWeatherList(url);
    }
}
