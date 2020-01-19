package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.location.GPSLocation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GPSLocationRepository extends CrudRepository<GPSLocation, Long> {
}
