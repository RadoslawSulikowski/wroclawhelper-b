package com.wroclawhelperb.service.mail;

import com.wroclawhelperb.domain.bike.BikeStationDto;
import com.wroclawhelperb.domain.statistics.EmailsSentStatistic;
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

    public EmailScheduler(EmailService emailService, UserRepository userRepository, WeatherService weatherService,
                          VozillaService carService, BikeService bikeService,
                          EmailsSentStatisticsRepository statisticsRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.weatherService = weatherService;
        this.carService = carService;
        this.bikeService = bikeService;
        this.statisticsRepository = statisticsRepository;
    }

    @Scheduled(cron = "0 00 07 * * *")
    private void sendEmail() {
        LOGGER.info("Scheduler started...");
        int userCounter = 0;
        int emailCounter = 0;
        for (User user : userRepository.findAll()) {
            userCounter++;
            if (user.isSchedulerOn()) {
                try {
                    emailService.send(prepareMail(user));
                    emailCounter++;
                } catch(Exception e) {
                    LOGGER.error("Email to " + user.getUserName() + " not sent: " + e.getMessage(), e);
                }
            }
        }
        statisticsRepository.save(new EmailsSentStatistic(LocalDateTime.now(), userCounter, emailCounter));
    }

    public Mail prepareMail(User user) throws UserNotFoundException {
        WeatherDtoNoId weather = weatherService.getWeatherOnNearestStation(user.getId());
        return new Mail.MailBuilder().sendTo(user.getEmail()).subject(SUBJECT)
                .messageLine("\tGood morning, " + user.getFirstName() + "!\n")
                .messageLine("Are you wondering what the weather is like outside the window?")
                .messageLine("Luckily your scheduler on wroclawhelper is ON!\n")
                .messageLine("The nearest Weather Station from location which your set in your profile is on:")
                .messageLine(weather.getWeatherStationName() + "\n")
                .messageLine("The weather was updated " + weather.getMeasuringTime().toLocalDate() + " "
                        + weather.getMeasuringTime().toLocalTime())
                .messageLine("Wind:\t\t\t\t\t   " + weather.getWindSpeed() + "m/s " + weather.getStringWindDirection())
                .messageLine("Air temperature:\t\t\t" + weather.getAirTemperature() + "\u00B0C")
                .messageLine("Ground temperature:\t\t  " + weather.getGroundTemperature() + "\u00B0C")
                .messageLine("Precipitation type:\t\t\t" + weather.getEnglishPrecipitationType())
                .messageLine("Air humidity:\t\t\t\t  " + weather.getHumidity() + "%\n")
                .messageLine(whatToGo(user, weather))
                .messageLine("\nHave a nice day!")
                .messageLine("\t\tyour woclawhelper!")
                .build();
    }

    public String whatToGo(User user, WeatherDtoNoId weather) throws UserNotFoundException {
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
            return String.format("%1$s %2$s\n%3$s\n%4$s", msg, line1, line2, line3);
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
            return String.format("%1$s %2$s\n\n%3$s\n%4$s", msg, line1, line2, line3);
        }
    }
}
