package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.statistics.LoginAttemptsStatistic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LoginAttemptsStatisticsRepository extends CrudRepository<LoginAttemptsStatistic, Long> {

}
