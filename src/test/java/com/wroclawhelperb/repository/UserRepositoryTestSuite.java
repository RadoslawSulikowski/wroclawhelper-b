package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.exception.UserNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.wroclawhelperb.domain.location.GPSLocation.USER_FAVORITE_LOCATION;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryTestSuite {

    @Autowired
    private UserRepository userRepository;

    private static Long userId;

    @Test
    @Order(1)
    void testSaveUser() {
        //Given
        User user = new User("TestFirstName", "TestLastName", "TestUserName",
                "TestPassword", "TestEmail",
                new GPSLocation(51.138235, 16.973045, USER_FAVORITE_LOCATION), true);

        //When
        user = userRepository.save(user);
        userId = user.getId();

        //Then
        assertTrue(userRepository.findById(userId).isPresent());

        //CleanUp in testDeleteUser
    }

    @Test
    @Order(2)
    void testFindByUserName() throws UserNotFoundException {
        //Given
        //When
        User user = userRepository.findByUserName("TestUserName").orElseThrow(UserNotFoundException::new);
        GPSLocation location = user.getLocation();

        //Then
        assertEquals(userId, user.getId());
        assertEquals("TestFirstName", user.getFirstName());
        assertEquals("TestLastName", user.getLastName());
        assertEquals("TestUserName", user.getUserName());
        assertEquals("TestPassword", user.getPassword());
        assertEquals("TestEmail", user.getEmail());
        assertTrue(user.isSchedulerOn());
        assertEquals(51.138235, user.getLocation().getLatitude(), 0);
        assertEquals(16.973045, user.getLocation().getLongitude(), 0);
        assertEquals(USER_FAVORITE_LOCATION, user.getLocation().getLocationType());
    }

    @Test
    @Order(3)
        //this test should always be the last - it cleans DB
    void testDeleteUser() {
        //Given
        assertTrue(userRepository.findById(userId).isPresent());
        //When
        userRepository.deleteById(userId);
        //
        assertFalse(userRepository.findById(userId).isPresent());
    }
}
