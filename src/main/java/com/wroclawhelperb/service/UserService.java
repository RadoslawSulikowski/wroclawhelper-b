package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoFull;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoPassword;
import com.wroclawhelperb.encryptor.Encryptor;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.UserMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDtoFull getUserByUsername(String username) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + username + "...");
        if (userRepository.findByUserName(username).isPresent()) {
            LOGGER.info("Fetching user with id " + username + "...");
            return userMapper.mapToUserDtoFull(userRepository.findByUserName(username).get());
        } else {
            LOGGER.error("There is no user with id " + username);
            throw new UserNotFoundException();
        }
    }

    public UserDtoNoPassword getUserById(Long id) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + id + "...");
        if (userRepository.findById(id).isPresent()) {
            LOGGER.info("Fetching user with id " + id + "...");
            return userMapper.mapToUserDto(userRepository.findById(id).get());
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
        return user.getId();
    }

    public UserDtoNoPassword updateUser(UserDtoFull userDto) throws UserNotFoundException {
        LOGGER.info("Searching for  user with id " + userDto.getId() + " to update...");
        if (userRepository.findById(userDto.getId()).isPresent()) {
            User user = userRepository.findById(userDto.getId()).get();
            LOGGER.info("User found, updating...");
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setUserName(userDto.getUserName());
            user.setPassword(Encryptor.encrypt(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.getLocation().setLatitude(userDto.getLocation().getLatitude());
            user.getLocation().setLongitude(userDto.getLocation().getLongitude());
            return userMapper.mapToUserDto(userRepository.save(user));
        } else {
            LOGGER.error("There's no user with id " + userDto.getId());
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

}
