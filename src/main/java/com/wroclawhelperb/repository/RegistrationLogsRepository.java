package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.statistics.RegistrationLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RegistrationLogsRepository extends CrudRepository<RegistrationLog, Long> {

}
