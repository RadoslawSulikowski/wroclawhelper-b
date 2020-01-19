package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.weather.Weather;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface WeatherRepository extends CrudRepository<Weather, Long> {
}
