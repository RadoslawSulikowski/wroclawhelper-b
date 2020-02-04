package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoIdNoPassword;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserAlreadyExistException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED, reason = "User successful added")
    public void addUser(@RequestBody UserDtoNoId userDto) throws UserAlreadyExistException {
        userService.addUser(userDto);
    }

    @GetMapping
    public List<UserDtoNoIdNoPassword> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/id/{userId}")
    public UserDtoNoIdNoPassword getUser(@PathVariable(name = "userId") Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    @GetMapping(value = "/username/{username}")
    public UserDtoNoIdNoPassword getUserByUserName(@PathVariable(name = "username") String username) throws UserNotFoundException {
        return userService.getUserByUsername(username);
    }

    @GetMapping(value = "/verify")
    public boolean verifyUser(@RequestBody UserDtoUsernamePassword user) {
        return userService.verifyUser(user);
    }

    @GetMapping(value = "/verify/{username}")
    public boolean isUsernameUnique(@PathVariable(name = "username") String username) {
        return userService.isUsernameUnique(username);
    }

    @PutMapping
    public UserDtoNoIdNoPassword updateUser(@RequestBody UserDtoNoIdNoPassword userDto) throws UserNotFoundException {
        return userService.updateUser(userDto);
    }

    @PatchMapping
    public UserDtoNoIdNoPassword updateUserProperty(@RequestBody Map<String, String> propertyValueMap)
            throws UserNotFoundException, NoUsernameInMapException {
        return userService.updateUserProperty(propertyValueMap);
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "User successful deleted")
    public void deleteUser(@PathVariable(name = "userId") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }
}
