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
@Entity(name = "RegistrationArchive")
@Table(name = "registrationArchive")
public class RegistrationArchive {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    private String username;

    public RegistrationArchive(LocalDateTime date, String username) {
        this.date = date;
        this.username = username;
    }
}
