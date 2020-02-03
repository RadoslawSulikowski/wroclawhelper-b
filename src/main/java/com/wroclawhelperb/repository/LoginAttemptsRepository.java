package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.statistics.LoginAttempt;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface LoginAttemptsRepository extends CrudRepository<LoginAttempt, Long> {

}
