package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.weather.WeatherStation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WeatherStationRepository extends CrudRepository<WeatherStation, String> {

    @Query(nativeQuery = true)
    WeatherStation fetchWeatherStationByName(@Param("sName") String name);
}
