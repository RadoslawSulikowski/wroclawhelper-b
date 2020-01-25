package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.vozilla.VozillaCarDto;
import com.wroclawhelperb.domain.vozilla.VozillaCarDtoList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
public class VozillaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VozillaService.class);

    private final RestTemplate restTemplate;

    @Value("${vozilla.car.api.endpoint}")
    String sourceUrl;


    public VozillaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private URI createUri() {
        return UriComponentsBuilder.fromHttpUrl(sourceUrl).build().encode().toUri();
    }

    public List<VozillaCarDto> getVozillaCarList() {
        try {
            return ofNullable(restTemplate.getForObject(createUri(), VozillaCarDtoList.class).getCars()).orElse(new ArrayList<>());

        } catch(RestClientException e) {
            LOGGER.error(e.getMessage(), e);
            return new ArrayList<>();
        }
    }
}
