package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.UserMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.wroclawhelperb.domain.location.GPSLocation.USER_FAVORITE_LOCATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTestSuite {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @MockBean
    private UserMapper mapper;

    @Test
    void testGetUserByUsernameShouldReturnUserDtoNoId() throws UserNotFoundException {
        //Given
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoId userDto = new UserDtoNoId("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(mapper.mapToUserDtoNoId(any(User.class))).thenReturn(userDto);

        //When
        UserDtoNoId returnedUserDto = service.getUserByUsername("A");

        //Then
        assertEquals(userDto, returnedUserDto);
    }

    @Test
    void testVerifyUserShouldReturnTrue() throws UserNotFoundException {
        //Given
        GPSLocation location = new GPSLocation(1.0, 1.5, USER_FAVORITE_LOCATION);
        location.setId(12L);
        User user = new User("a", "aa", "aaa", "A", "AA",
                location, false);
        user.setId(123L);
        Optional<User> userOpt = Optional.of(user);
        UserDtoUsernamePassword userToVerify =
                new UserDtoUsernamePassword("aaa", "A");
        when(repository.findByUserName(anyString())).thenReturn(userOpt);


        //When
        boolean result = service.verifyUser(userToVerify);

        //Then
        assertTrue(result);
    }

    @Test
    void testVerifyUserShouldReturnFalse() throws UserNotFoundException {
        //Given
        GPSLocation location = new GPSLocation(1.0, 1.5, USER_FAVORITE_LOCATION);
        location.setId(12L);
        User user = new User("a", "aa", "aaa", "A", "AA",
                location, false);
        user.setId(123L);
        Optional<User> userOpt = Optional.of(user);
        UserDtoUsernamePassword userToVerify =
                new UserDtoUsernamePassword("aaa", "a");
        when(repository.findByUserName(anyString())).thenReturn(userOpt);


        //When
        boolean result = service.verifyUser(userToVerify);

        //Then
        assertFalse(result);
    }

    @Test
    void shouldUpdateUserPropertyThrowNoUsernameInMapException() {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("someKey", "someValue");
        map.put("someOtherKey", "someOtherValue");

        //When & Then
        assertThrows(NoUsernameInMapException.class, () -> service.updateUserProperty(map));
    }

    @Test
    void shouldUpdateUserPropertyThrowUserNotFoundException() throws UserNotFoundException {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("username", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(repository.findByUserName(anyString())).thenThrow(UserNotFoundException.class);

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.updateUserProperty(map));

    }

    @Test
    void shouldUpdateUserPropertyReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoId userDto = new UserDtoNoId("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenReturn(user);
        when(mapper.mapToUserDtoNoId(any(User.class))).thenReturn(userDto);

        //When
        UserDtoNoId returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

}
