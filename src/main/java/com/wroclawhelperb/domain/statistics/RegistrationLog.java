package com.wroclawhelperb.domain.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "RegistrationLogs")
@Table(name = "registrationLogs")
public class RegistrationLog {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    private String username;

    public RegistrationLog(LocalDateTime date, String username) {
        this.date = date;
        this.username = username;
    }
}
