package com.wroclawhelperb.domain.user;

import com.wroclawhelperb.domain.GPSLocation;
import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.encryptor.Encryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "userName")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private GPSLocation location;


    public User(String firstName, String lastName, String userName, String password, String email, GPSLocation location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = Encryptor.encrypt(password);
        this.email = email;
        this.location = location;
    }

    public Locable findNearest(List<Locable> locableList) {
        if (locableList != null && locableList.size() > 0) {
            Locable locable = locableList.get(0);
            for (Locable l : locableList) {
                if (this.getLocation().getDistance(l.getLocation()) < this.getLocation().getDistance(locable.getLocation())) {
                    locable = l;
                }
            }
            return locable;
        }
        //throw some Exception?
        return null;
    }

}
