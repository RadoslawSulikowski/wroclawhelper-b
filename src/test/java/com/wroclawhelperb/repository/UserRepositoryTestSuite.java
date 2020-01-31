package com.wroclawhelperb.repository;

import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserRepositoryTestSuite {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        //Given
        User user = new User("a", "a", "a", "a", "a",
                new GPSLocation(1.0, 1.0, GPSLocation.USER_FAVORITE_LOCATION), true);

        //When
        user = userRepository.save(user);
        Long userId = user.getId();

        //Then
        assertTrue(userRepository.findById(userId).isPresent());

        //CleanUp
        userRepository.deleteById(userId);
    }
}
