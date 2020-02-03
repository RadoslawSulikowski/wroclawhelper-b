package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.statistics.LoginAttemptsStatistic;
import com.wroclawhelperb.domain.statistics.RegistrationArchive;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoPassword;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.UserMapper;
import com.wroclawhelperb.repository.LoginAttemptsStatisticsRepository;
import com.wroclawhelperb.repository.RegistrationArchiveRepository;
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
    private final LoginAttemptsStatisticsRepository loginAttemptsStatisticsRepository;
    private final RegistrationArchiveRepository registrationArchiveRepository;

    public UserService(final UserRepository userRepository, final UserMapper userMapper,
                       final LoginAttemptsStatisticsRepository loginAttemptsStatisticsRepository,
                       final RegistrationArchiveRepository registrationArchiveRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.loginAttemptsStatisticsRepository = loginAttemptsStatisticsRepository;
        this.registrationArchiveRepository = registrationArchiveRepository;
    }

    public UserDtoNoId getUserByUsername(String username) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + username + "...");
        if (userRepository.findByUserName(username).isPresent()) {
            LOGGER.info("Fetching user with id " + username + "...");
            return userMapper.mapToUserDtoNoId(userRepository.findByUserName(username).get());
        } else {
            LOGGER.error("There is no user with id " + username);
            throw new UserNotFoundException();
        }
    }

    public UserDtoNoId getUserById(Long id) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + id + "...");
        if (userRepository.findById(id).isPresent()) {
            LOGGER.info("Fetching user with id " + id + "...");
            return userMapper.mapToUserDtoNoId(userRepository.findById(id).get());
        } else {
            LOGGER.error("There is no user with id " + id);
            throw new UserNotFoundException();
        }
    }

    public List<UserDtoNoPassword> getAllUsers() {
        LOGGER.info("Fetching users from database...");
        List<UserDtoNoPassword> userDtos = new ArrayList<>();
        userRepository.findAll().forEach(u -> userDtos.add(userMapper.mapToUserDto(u)));
        return userDtos;
    }

    public Long addUser(UserDtoNoId userDto) {
        User user = userRepository.save(userMapper.mapToUser(userDto));
        LOGGER.info("User correctly stored with id: " + user.getId() + ".");
        registrationArchiveRepository.save(new RegistrationArchive(LocalDateTime.now(), user.getUserName()));
        return user.getId();
    }

    public UserDtoNoId updateUser(UserDtoNoId userDto) throws UserNotFoundException {
        LOGGER.info("Searching for  user with username " + userDto.getUserName() + " to update...");
        if (userRepository.findByUserName(userDto.getUserName()).isPresent()) {
            User user = userRepository.findByUserName(userDto.getUserName()).get();
            LOGGER.info("User found, updating...");
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPassword(userDto.getPassword());
            user.setEmail(userDto.getEmail());
            user.getLocation().setLatitude(userDto.getLocation().getLatitude());
            user.getLocation().setLongitude(userDto.getLocation().getLongitude());
            user.setSchedulerOn(userDto.isSchedulerOn());
            return userMapper.mapToUserDtoNoId(userRepository.save(user));
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

    public boolean verifyUser(UserDtoUsernamePassword user){
        boolean attemptResult = (userRepository.findByUserName(user.getUsername()).isPresent()
                && userRepository.findByUserName(user.getUsername()).get().getPassword().equals(user.getPassword()));
        loginAttemptsStatisticsRepository.save(
                new LoginAttemptsStatistic(LocalDateTime.now(), user.getUsername(), attemptResult));
        return attemptResult;
    }

    public UserDtoNoId updateUserProperty(Map<String, String> propertyValueMap)
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
        return userMapper.mapToUserDtoNoId(userRepository.save(user));
    }
}
