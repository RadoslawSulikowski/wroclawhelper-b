package com.wroclawhelperb.service;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoIdNoPassword;
import com.wroclawhelperb.domain.user.UserDtoUsernamePassword;
import com.wroclawhelperb.exception.NoUsernameInMapException;
import com.wroclawhelperb.exception.UserAlreadyExistException;
import com.wroclawhelperb.exception.UserNotFoundException;
import com.wroclawhelperb.mapper.UserMapper;
import com.wroclawhelperb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static com.wroclawhelperb.domain.location.GPSLocation.USER_FAVORITE_LOCATION;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
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
    void shouldGetUserByUsernameReturnUserDtoNoId() throws UserNotFoundException {
        //Given
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUserDto = service.getUserByUsername(anyString());

        //Then
        assertEquals(userDto, returnedUserDto);
    }

    @Test
    void shouldGetUserByUsernameThrowUserNotFoundException() {
        //Given
        when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.getUserByUsername(anyString()));
    }

    @Test
    void shouldGetUserByIdReturnUserDtoNoId() throws UserNotFoundException {
        //Given
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUserDto = service.getUserById(anyLong());

        //Then
        assertEquals(userDto, returnedUserDto);
    }

    @Test
    void shouldGetUserByIdThrowUserNotFoundException() {
        //Given
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.getUserById(anyLong()));
    }

    @Test
    void testVerifyUserShouldReturnTrue() {
        //Given
        GPSLocation location = new GPSLocation(1.0, 1.5, USER_FAVORITE_LOCATION);
        location.setId(12L);
        User user = new User("a", "aa", "aaa", "A", "AA",
                location, false);
        user.setId(123L);
        UserDtoUsernamePassword userToVerify =
                new UserDtoUsernamePassword("aaa", "A");
        when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));

        //When
        boolean result = service.verifyUser(userToVerify);

        //Then
        assertTrue(result);
    }

    @Test
    void testVerifyUserShouldReturnFalse() {
        //Given
        GPSLocation location = new GPSLocation(1.0, 1.5, USER_FAVORITE_LOCATION);
        location.setId(12L);
        User user = new User("a", "aa", "aaa", "A", "AA",
                location, false);
        user.setId(123L);
        UserDtoUsernamePassword userToVerify =
                new UserDtoUsernamePassword("aaa", "a");
        when(repository.findByUserName(anyString())).thenReturn(Optional.of(user));

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
    void shouldUpdateUserPropertyThrowUserNotFoundException() {
        //Given
        Map<String, String> map = new HashMap<>();
        map.put("username", "someValue");
        map.put("someOtherKey", "someOtherValue");
        when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.updateUserProperty(map));
    }

    @Test
    void shouldUpdateUserPropertyFirstNameReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("updatedFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("updatedFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("firstName", "updatedFirstName");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertyLastNameReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "updatedLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "updatedLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("lastName", "updatedLastName");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertyPasswordReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "updatedPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("password", "updatedPassword");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertyEmailReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "updatedEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "updatedEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("email", "updatedEmail");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertyLatitudeReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(50.0001, 16.973045, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(50.0001, 16.973045, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("latitude", "50.0001");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertyLongitudeReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 17.0001, USER_FAVORITE_LOCATION), true);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 17.0001, USER_FAVORITE_LOCATION), true);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("longitude", "17.0001");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserPropertySchedulerOnReturnUpdatedUser() throws UserNotFoundException, NoUsernameInMapException {
        //Given
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), false);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("TestFirstName", "TestLastName",
                "TestUserName", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), false);
        Map<String, String> map = new HashMap<>();
        map.put("username", "TestUserName");
        map.put("schedulerOn", "false");
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUserProperty(map);

        //Then
        assertEquals(userDto, returnedUser);
    }

    @Test
    void shouldUpdateUserThrowUserNotFoundException() {
        //Given
        when(repository.findByUserName(anyString())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.updateUser(new UserDtoNoIdNoPassword()));
    }

    @Test
    void shouldUpdateUserReturnUpdatedUser() throws UserNotFoundException {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        User userToUpdate = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);
        User user = new User("UpdatedFirstName", "UpdatedLastName", "TestUserName",
                "UpdatedPassword", "UpdatedEmail",
                new GPSLocation(51.00005, 16.000045, USER_FAVORITE_LOCATION), false);
        UserDtoNoIdNoPassword userDto = new UserDtoNoIdNoPassword("UpdatedFirstName",
                "UpdatedLastName", "TestUserName", "UpdatedEmail",
                new GPSLocation(51.00005, 16.000045, USER_FAVORITE_LOCATION), false);
        when(repository.findByUserName("TestUserName")).thenReturn(Optional.of(userToUpdate));
        when(repository.save(userToUpdate)).thenReturn(user);
        when(mapper.mapToUserDtoNoIdNoPassword(user)).thenReturn(userDto);

        //When
        UserDtoNoIdNoPassword returnedUser = service.updateUser(userDto);

        //Then
        assertEquals(userDto, returnedUser);
        assertThat(logsList).extracting("message")
                .contains("Searching for  user with username TestUserName to update..."
                        , "User found, updating...");
    }

    @Test
    void shouldDeleteUserThrowUserNotFoundException() {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(UserNotFoundException.class, () -> service.deleteUser(1L));
        assertThat(logsList).extracting("message")
                .contains("Searching for  user with id 1 to delete...", "There is no user with id 1");
    }

    @Test
    void shouldDeleteUser() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        List<ILoggingEvent> logsList = prepareLogList();
        doNothing().when(repository).deleteById(anyLong());
        when(repository.findById(anyLong())).thenReturn(Optional.of(user));

        //When
        service.deleteUser(1L);

        //Then
        assertThat(logsList).extracting("message")
                .contains("Searching for  user with id 1 to delete...", "User with id 1 deleted.");
    }

    @Test
    void shouldAddUserReturnSavedUserId() throws UserAlreadyExistException {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        User userToSave = new User();
        User savedUser = new User();
        savedUser.setId(1L);
        UserDtoNoId userDto = new UserDtoNoId();
        when(mapper.mapToUser(userDto)).thenReturn(userToSave);
        when(repository.save(userToSave)).thenReturn(savedUser);

        //When
        Long savedUserId = service.addUser(userDto);

        //Then
        assertEquals(1L, savedUserId);
        assertThat(logsList).extracting("message")
                .containsOnly("User correctly stored with id: 1.");
    }

    @Test
    void shouldGetAllUsersReturnEmptyList() {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();
        when(repository.findAll()).thenReturn(emptyList());

        //When
        List<UserDtoNoIdNoPassword> fetchedList = service.getAllUsers();

        //Then
        assertEquals(0, fetchedList.size());
        assertThat(logsList).extracting("message")
                .containsOnly("Fetching users from database...");
    }

    @Test
    void shouldGetAllUsersReturnUsersList() {
        //Given
        List<ILoggingEvent> logsList = prepareLogList();

        User user1 = new User("fName1", "lName1", "uName1", "pass1",
                "mail1", new GPSLocation(1.0, 1.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        user1.setId(1L);
        User user2 = new User("fName2", "lName2", "uName2", "pass2",
                "mail2", new GPSLocation(2.0, 2.0, GPSLocation.USER_FAVORITE_LOCATION),
                false);
        user2.setId(2L);
        UserDtoNoIdNoPassword userDto1 = new UserDtoNoIdNoPassword("fName1", "lName1", "uName1",
                "mail1", new GPSLocation(1.0, 1.0, GPSLocation.USER_FAVORITE_LOCATION),
                true);
        UserDtoNoIdNoPassword userDto2 = new UserDtoNoIdNoPassword("fName2", "lName2", "uName2",
                "mail2", new GPSLocation(2.0, 2.0, GPSLocation.USER_FAVORITE_LOCATION),
                false);
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        List<UserDtoNoIdNoPassword> userDtos = new ArrayList<>();
        userDtos.add(userDto1);
        userDtos.add(userDto2);
        when(mapper.mapToUserDtoNoIdNoPassword(user1)).thenReturn(userDto1);
        when(mapper.mapToUserDtoNoIdNoPassword(user2)).thenReturn(userDto2);
        when(repository.findAll()).thenReturn(users);

        //When
        List<UserDtoNoIdNoPassword> fetchedList = service.getAllUsers();

        //Then
        assertEquals(userDtos, fetchedList);
        assertThat(logsList).extracting("message")
                .containsOnly("Fetching users from database...");
    }


    private List<ILoggingEvent> prepareLogList() {
        Logger userServiceLogger = (Logger) LoggerFactory.getLogger(UserService.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        userServiceLogger.addAppender(listAppender);
        return listAppender.list;
    }

}
