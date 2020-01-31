package com.wroclawhelperb.service;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoFull;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.encryptor.Encryptor;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.wroclawhelperb.domain.location.GPSLocation.USER_FAVORITE_LOCATION;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTestSuite {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    void testGetUSerByUsernameShouldReturnUserDtoFull() throws UserNotFoundException {
        //Given
        GPSLocation location = new GPSLocation(1.0, 1.5, USER_FAVORITE_LOCATION);
        location.setId(12L);
        User user = new User("a", "aa", "aaa", "A", "AA",
                location, false);
        user.setId(123L);
        Optional<User> userOpt = Optional.of(user);
        when(repository.findByUserName(anyString())).thenReturn(userOpt);

        //When
        UserDtoFull returnedUserDto = service.getUserByUsername("A");

        //Then
        assertEquals("a", returnedUserDto.getFirstName());
        assertEquals("aa", returnedUserDto.getLastName());
        assertEquals("aaa", returnedUserDto.getUserName());
        assertEquals(Encryptor.encrypt("A"), returnedUserDto.getPassword());
        assertEquals("AA", returnedUserDto.getEmail());
        assertEquals(123L, returnedUserDto.getId());
        assertEquals(12L, location.getId());
        assertFalse(returnedUserDto.isSchedulerOn());
        assertEquals(1.0, location.getLatitude(), 0);
        assertEquals(1.5, location.getLongitude(), 0);
        assertEquals(USER_FAVORITE_LOCATION, location.getLocationType());
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
                new UserDtoUsernamePassword("aaa", Encryptor.encrypt("A"));
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
                new UserDtoUsernamePassword("aaa", Encryptor.encrypt("a"));
        when(repository.findByUserName(anyString())).thenReturn(userOpt);


        //When
        boolean result = service.verifyUser(userToVerify);

        //Then
        assertFalse(result);

    }

}
