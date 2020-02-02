package com.wroclawhelperb.domain.user;

import com.wroclawhelperb.domain.location.GPSLocation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserDtoNoId {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private String email;
    private GPSLocation location;
    private boolean schedulerOn;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UserDtoNoId that = (UserDtoNoId) o;

        if (schedulerOn != that.schedulerOn) {
            return false;
        }
        if (!firstName.equals(that.firstName)) {
            return false;
        }
        if (!lastName.equals(that.lastName)) {
            return false;
        }
        if (!userName.equals(that.userName)) {
            return false;
        }
        if (!password.equals(that.password)) {
            return false;
        }
        if (!email.equals(that.email)) {
            return false;
        }
        return location.equals(that.location);

    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + userName.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + (schedulerOn ? 1 : 0);
        return result;
    }
}
