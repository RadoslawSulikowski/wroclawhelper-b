package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.weather.Weather;
import com.wroclawhelperb.repository.WeatherStationRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.wroclawhelperb.domain.weather.WeatherStation.nameToShortName;

@Component
public class CSVMapper {

    private final WeatherStationRepository weatherStationRepository;

    public CSVMapper(WeatherStationRepository weatherStationRepository) {
        this.weatherStationRepository = weatherStationRepository;
    }

    public List<Weather> mapToWeatherList(URL url) throws IOException {
        Scanner scanner = new Scanner(url.openStream(), "UTF-8");
        List<Weather> weatherList = new ArrayList<>();
        int lineCounter = 0;
        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(",");
            System.out.println(Arrays.toString(line));
            if (lineCounter > 1) {
                weatherList.add(new Weather(
                        Long.parseLong(line[0]),
                        LocalDateTime.parse(line[1].substring(0, 10) + "T" + line[1].substring(11, 16)),
                        Double.parseDouble(line[2]),
                        Double.parseDouble(line[3]),
                        Double.parseDouble(line[4]),
                        Double.parseDouble(line[5]),
                        Double.parseDouble(line[6]),
                        line[7],
                        weatherStationRepository.findById(nameToShortName(line[8])).get()
                ));
            }
            lineCounter++;
        }
        scanner.close();
        return weatherList;
    }

    public List<Weather> mapToWeatherList(String source) throws IOException {
        URL url = new URL(source);
        return mapToWeatherList(url);
    }
}
