package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.encryptor.Encryptor;
import com.wroclawhelperb.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.wroclawhelperb.domain.location.GPSLocation.USER_FAVORITE_LOCATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTestSuite {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        //Given
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(1.0, 1.0, USER_FAVORITE_LOCATION), true);

        //When
        user = userRepository.save(user);
        Long userId = user.getId();

        //Then
        assertTrue(userRepository.findById(userId).isPresent());

        //CleanUp
        userRepository.deleteById(userId);
    }

    @Test
    void testFindByUserName() throws UserNotFoundException{
        //Given
        //When
        User user = userRepository.findByUserName("a").orElseThrow(UserNotFoundException::new);
        GPSLocation location = user.getLocation();

        //Then
        assertEquals("A", user.getFirstName());
        assertEquals("a", user.getLastName());
        assertEquals(Encryptor.encrypt("a"), user.getPassword());
        assertEquals("a", user.getEmail());
        assertEquals("A", user.getFirstName());
        assertEquals(17, user.getId());
        assertEquals("a", user.getUserName());
        assertEquals(18, location.getId());
        assertTrue(user.isSchedulerOn());
        assertEquals(51.138235, location.getLatitude(), 0);
        assertEquals(16.973045, location.getLongitude(), 0);
        assertEquals(USER_FAVORITE_LOCATION, location.getLocationType());

    }
}
