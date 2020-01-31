package com.wroclawhelperb.domain.user;

import com.wroclawhelperb.domain.Locable;
import com.wroclawhelperb.domain.location.GPSLocation;
import com.wroclawhelperb.encryptor.Encryptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity(name = "Users")
@Table(name = "users")
public class User implements Locable {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "userName", unique = true)
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "schedulerOn")
    private boolean schedulerOn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private GPSLocation location;


    public User(String firstName, String lastName, String userName, String password,
                String email, GPSLocation location, boolean schedulerOn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = Encryptor.encrypt(password);
        this.email = email;
        this.location = location;
        this.schedulerOn = schedulerOn;
    }
}
