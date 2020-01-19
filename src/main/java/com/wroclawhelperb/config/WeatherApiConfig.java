package com.wroclawhelperb.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class WeatherApiConfig {

    @Value("${weather.api.endpoint}")
    String surl;
}
