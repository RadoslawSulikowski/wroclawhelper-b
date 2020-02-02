package com.wroclawhelperb.service.mail;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.email.counter.EmailsSentStatistic;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.domain.weather.WeatherDtoNoId;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.repository.EmailsSentStatisticsRepository;
import com.wroclawhelperb.repository.UserRepository;
import com.wroclawhelperb.service.BikeService;
import com.wroclawhelperb.service.VozillaService;
import com.wroclawhelperb.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EmailScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailScheduler.class);
    private static final String SUBJECT = "What's the weather like today in Wroclaw";

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final WeatherService weatherService;
    private final VozillaService carService;
    private final BikeService bikeService;
    private final EmailsSentStatisticsRepository statisticsRepository;

    public EmailScheduler(EmailService emailService, UserRepository userRepository, WeatherService weatherService, VozillaService carService, BikeService bikeService, EmailsSentStatisticsRepository statisticsRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.weatherService = weatherService;
        this.carService = carService;
        this.bikeService = bikeService;
        this.statisticsRepository = statisticsRepository;
    }

    @Scheduled(cron = "0 02 18 * * *")
    private void sendEmail() {
        LOGGER.info("Scheduler started...");
        int userCounter = 0;
        int emailCounter = 0;
        for (User user : userRepository.findAll()) {
            userCounter++;
            if (user.isSchedulerOn()) {
                try {
                    emailService.send(new Mail(user.getEmail(), SUBJECT, prepareMessage(user)));
                    emailCounter++;
                } catch(Exception e) {
                    LOGGER.error("Email to " + user.getUserName() + " not sent: " + e.getMessage(), e);
                }
            }
        }
        statisticsRepository.save(new EmailsSentStatistic(LocalDateTime.now(), userCounter, emailCounter));
    }

    private String prepareMessage(User user) throws UserNotFoundException {
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStation(user.getId());
        String line1 = "\tGood morning, " + user.getFirstName() + "!\n";
        String line2 = "Are you wondering what the weather is like outside the window?\n";
        String line3 = "Luckily your scheduler on wroclawhelper is ON!\n\n";
        String line4 = "The nearest Weather Station from location which your set in your profile is on:\n";
        String line5 = weather.getWeatherStationName() + "\n\n";
        String line6 = "The weather was updated " + weather.getMeasuringTime().toLocalDate() + " "
                + weather.getMeasuringTime().toLocalTime() + "\n";
        String line7 = "Wind:\t\t\t\t\t   " + weather.getWindSpeed() + "m/s " + weather.getStringWindDirection() + "\n";
        String line8 = "Air temperature:\t\t\t" + weather.getAirTemperature() + "\u00B0C\n";
        String line9 = "Ground temperature:\t\t  " + weather.getGroundTemperature() + "\u00B0C\n";
        String line10 = "Precipitation type:\t\t\t" + weather.getEnglishPrecipitationType() + "\n";
        String line11 = "Air humidity:\t\t\t\t  " + weather.getHumidity() + "%\n";

        return String.format("%1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s%10$s%11$s%12$s",
                line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, whatToGo(user, weather));
    }

    private String whatToGo(User user, WeatherDtoNoId weather) throws UserNotFoundException {
        boolean goByCar = false;
        String msg = "\tToday";
        if (weather.getWindSpeed() >= 8) {
            msg = String.format("%1$s %2$s", msg, "is windy,");
            goByCar = true;
        }
        if (!weather.getEnglishPrecipitationType().equals("NONE")) {
            msg = String.format("%1$s %2$s", msg, "is precipitation,");
            goByCar = true;
        }
        if (weather.getAirTemperature() < 0) {
            msg = String.format("%1$s %2$s", msg, "is frost,");
            goByCar = true;
        }
        if (goByCar) {
            VozillaCarDto car = carService.getNearestAvailableCar(user.getId());
            String line1 = "so you should go by car.";
            String line2 = "The nearest available Vozilla car(" + car.getColor() + " " + car.getName() + " "
                    + car.getPlatesNumber() + ") from set location you can find on:";
            String line3 = car.getAddress() + " under coordinates: " + car.getLocation().getLatitude() + " "
                    + car.getLocation().getLongitude();
            msg = String.format("%1$s %2$s\n%3$s\n%4$s", msg, line1, line2, line3);
        } else {
            BikeStationDto bikeStation = bikeService.getNearestStationWithAvailableBike(user.getId());
            int availableBikes = bikeStation.getBikes() - bikeStation.getBookedBikes();
            String line1 = "is nice weather, so you should consider going by bike!";
            String line2;
            if (availableBikes == 1) {
                line2 = "The nearest WRM bike station with available 1 bike:";
            } else {
                line2 = "The nearest WRM bike station with available " + availableBikes + " bikes:";
            }
            String line3 = bikeStation.getName() + " under coordinates: " + bikeStation.getLocation().getLatitude()
                    + " " + bikeStation.getLocation().getLongitude();
            msg = String.format("%1$s %2$s\n\n%3$s\n%4$s", msg, line1, line2, line3);
        }
        String line1 = "Have a nice day!";
        String line2 = "your woclawhelper!";
        msg = String.format("%1$s\n\n%2$s\t\t\n%3$s", msg, line1, line2);
        return msg;
    }
}
