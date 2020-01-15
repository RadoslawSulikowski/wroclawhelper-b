package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoPassword;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDtoNoPassword mapToUserDto(User user) {
        return new UserDtoNoPassword(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getEmail(),
                user.getLocation()
        );
    }

    public User mapToUser(UserDtoNoId userDtoNoId) {
        return new User(
                userDtoNoId.getFirstName(),
                userDtoNoId.getLastName(),
                userDtoNoId.getUserName(),
                userDtoNoId.getPassword(),
                userDtoNoId.getEmail(),
                userDtoNoId.getLocation()
        );
    }
}
