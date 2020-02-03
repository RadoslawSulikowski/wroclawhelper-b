package com.wroclawhelperb.mapper;

import com.wroclawhelperb.domain.user.User;
import com.wroclawhelperb.domain.user.UserDtoNoId;
import com.wroclawhelperb.domain.user.UserDtoNoIdNoPassword;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDtoNoIdNoPassword mapToUserDtoNoIdNoPassword(User user) {
        return new UserDtoNoIdNoPassword(user.getFirstName(), user.getLastName(), user.getUserName(),
                user.getEmail(), user.getLocation(), user.isSchedulerOn());
    }

    public User mapToUser(UserDtoNoId userDtoNoId) {
        return new User(userDtoNoId.getFirstName(), userDtoNoId.getLastName(), userDtoNoId.getUserName(),
                userDtoNoId.getPassword(), userDtoNoId.getEmail(), userDtoNoId.getLocation(), userDtoNoId.isSchedulerOn());
    }
}
