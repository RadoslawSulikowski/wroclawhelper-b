package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.email.counter.EmailsSentStatistic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EmailsSentStatisticsRepository extends CrudRepository<EmailsSentStatistic, Long> {
}
