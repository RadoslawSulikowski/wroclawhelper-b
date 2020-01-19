package com.wroclawhelperb.domain.user;

import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDtoNoPassword {

    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private GPSLocation location;

}
