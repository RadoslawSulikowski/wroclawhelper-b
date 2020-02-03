package com.wroclawhelperb.domain.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "LoginAttempts")
@Table(name = "loginAttempts")
public class LoginAttempt {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    private String username;

    @Column(name = "attemptResult")
    private boolean attemptResult;

    public LoginAttempt(LocalDateTime date, String username, boolean attemptResult) {
        this.date = date;
        this.username = username;
        this.attemptResult = attemptResult;
    }
}
