package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.WeatherStation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface WeatherStationRepository extends CrudRepository<WeatherStation, Long> {
}
