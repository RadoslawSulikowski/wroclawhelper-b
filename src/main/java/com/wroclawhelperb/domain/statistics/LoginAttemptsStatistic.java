package com.wroclawhelperb.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity(name = "LoginAttemptsStatistics")
@Table(name = "loginAttemptsStatistics")
public class LoginAttemptsStatistic {

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

    public LoginAttemptsStatistic(LocalDateTime date, String username, boolean attemptResult) {
        this.date = date;
        this.username = username;
        this.attemptResult = attemptResult;
    }
}
