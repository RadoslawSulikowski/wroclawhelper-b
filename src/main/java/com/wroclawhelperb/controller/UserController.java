package com.wroclawhelperb.controller;

import com.wroclawhelperb.domain.user.UserDtoFull;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoPassword;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/users")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "User successful added")
    public Long addUser(@RequestBody UserDtoNoId userDto) {
        return userService.addUser(userDto);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users")
    public List<UserDtoNoPassword> getAllUsers() {
        return userService.getAllUsers();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/users/{userId}")
    public UserDtoNoPassword getUser(@PathVariable(name = "userId") Long id) throws UserNotFoundException {
        return userService.getUser(id);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/users")
    public UserDtoNoPassword updateUser(@RequestBody UserDtoFull userDto) throws UserNotFoundException {
        return userService.updateUser(userDto);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/users/{userId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT, reason = "User successful deleted")
    public void deleteUser(@PathVariable(name = "userId") Long id) throws UserNotFoundException {
        userService.deleteUser(id);
    }
}
