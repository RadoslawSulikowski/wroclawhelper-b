package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.exception.UserNotFoundException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUserName(String username) throws UserNotFoundException;
}
