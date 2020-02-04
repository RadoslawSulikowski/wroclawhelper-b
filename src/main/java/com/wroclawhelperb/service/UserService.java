package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.statistics.LoginAttempt;
import com.wroclawhelperb.domain.statistics.PasswordChangeLog;
import com.wroclawhelperb.domain.statistics.RegistrationLog;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoIdNoPassword;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserAlreadyExistException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.UserMapper;
import com.wroclawhelperb.repository.LoginAttemptsRepository;
import com.wroclawhelperb.repository.PasswordChangeLogsRepository;
import com.wroclawhelperb.repository.RegistrationLogsRepository;
import com.wroclawhelperb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Double.parseDouble;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginAttemptsRepository loginAttemptsRepository;
    private final RegistrationLogsRepository registrationLogsRepository;
    private final PasswordChangeLogsRepository passwordRepository;


    public UserService(final UserRepository userRepository, final UserMapper userMapper,
                       final LoginAttemptsRepository loginAttemptsRepository,
                       final RegistrationLogsRepository registrationLogsRepository,
                       final PasswordChangeLogsRepository passwordRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loginAttemptsRepository = loginAttemptsRepository;
        this.registrationLogsRepository = registrationLogsRepository;
        this.passwordRepository = passwordRepository;
    }

    public UserDtoNoIdNoPassword getUserByUsername(String username) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + username + "...");
        if (userRepository.findByUserName(username).isPresent()) {
            LOGGER.info("Fetching user with id " + username + "...");
            return userMapper.mapToUserDtoNoIdNoPassword(userRepository.findByUserName(username).get());
        } else {
            LOGGER.error("There is no user with id " + username);
            throw new UserNotFoundException();
        }
    }

    public UserDtoNoIdNoPassword getUserById(Long id) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + id + "...");
        if (userRepository.findById(id).isPresent()) {
            LOGGER.info("Fetching user with id " + id + "...");
            return userMapper.mapToUserDtoNoIdNoPassword(userRepository.findById(id).get());
        } else {
            LOGGER.error("There is no user with id " + id);
            throw new UserNotFoundException();
        }
    }

    public List<UserDtoNoIdNoPassword> getAllUsers() {
        LOGGER.info("Fetching users from database...");
        List<UserDtoNoIdNoPassword> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(u -> userDtos.add(userMapper.mapToUserDtoNoIdNoPassword(u)));
        return userDtos;
    }

    public Long addUser(UserDtoNoId userDto) throws UserAlreadyExistException {
        if (isUsernameUnique(userDto.getUserName())) {
            User user = userRepository.save(userMapper.mapToUser(userDto));
            LOGGER.info("User correctly stored with id: " + user.getId() + ".");
            registrationLogsRepository.save(new RegistrationLog(LocalDateTime.now(), user.getUserName()));
            return user.getId();
        }
        LOGGER.info("User with username" + userDto.getUserName() + "already exists!");
        throw new UserAlreadyExistException();
    }

    public UserDtoNoIdNoPassword updateUser(UserDtoNoIdNoPassword userDto) throws UserNotFoundException {
        LOGGER.info("Searching for  user with username " + userDto.getUserName() + " to update...");
        if (userRepository.findByUserName(userDto.getUserName()).isPresent()) {
            User user = userRepository.findByUserName(userDto.getUserName()).get();
            LOGGER.info("User found, updating...");
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.getLocation().setLatitude(userDto.getLocation().getLatitude());
            user.getLocation().setLongitude(userDto.getLocation().getLongitude());
            user.setSchedulerOn(userDto.isSchedulerOn());
            return userMapper.mapToUserDtoNoIdNoPassword(userRepository.save(user));
        } else {
            LOGGER.error("There's no user with id " + userDto.getUserName());
            throw new UserNotFoundException();
        }
    }

    public void deleteUser(Long id) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + id + " to delete...");
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            LOGGER.info("User with id " + id + " deleted.");
        } else {
            LOGGER.error("There is no user with id " + id);
            throw new UserNotFoundException();
        }
    }

    public boolean verifyUser(UserDtoUsernamePassword user) {
        boolean attemptResult = (userRepository.findByUserName(user.getUsername()).isPresent()
                && userRepository.findByUserName(user.getUsername()).get().getPassword().equals(user.getPassword()));
        loginAttemptsRepository.save(
                new LoginAttempt(LocalDateTime.now(), user.getUsername(), attemptResult));
        return attemptResult;
    }

    public UserDtoNoIdNoPassword updateUserProperty(Map<String, String> propertyValueMap)
            throws NoUsernameInMapException, UserNotFoundException {
        String username = propertyValueMap.entrySet()
                .stream()
                .filter(e -> e.getKey().equals("username"))
                .findFirst().orElseThrow(NoUsernameInMapException::new).getValue();
        User user = userRepository.findByUserName(username).orElseThrow(UserNotFoundException::new);
        for (Map.Entry<String, String> e : propertyValueMap.entrySet()) {
            if (e.getKey().equals("firstName")) {
                user.setFirstName(e.getValue());
            }
            if (e.getKey().equals("lastName")) {
                user.setLastName(e.getValue());
            }
            if (e.getKey().equals("password")) {
                passwordRepository.save(new PasswordChangeLog(LocalDateTime.now(), user.getUserName(),
                        user.getPassword(), e.getValue()));
                user.setPassword(e.getValue());
            }
            if (e.getKey().equals("email")) {
                user.setEmail(e.getValue());
            }
            if (e.getKey().equals("latitude")) {
                user.getLocation().setLatitude(parseDouble(e.getValue()));
            }
            if (e.getKey().equals("longitude")) {
                user.getLocation().setLongitude(parseDouble(e.getValue()));
            }
            if (e.getKey().equals("schedulerOn")) {
                user.setSchedulerOn(parseBoolean(e.getValue()));
            }
        }
        return userMapper.mapToUserDtoNoIdNoPassword(userRepository.save(user));
    }

    public boolean isUsernameUnique(String username) {
        return !(userRepository.findByUserName(username).isPresent());
    }
}
