package com.wroclawhelperb.domain.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "PasswordChangeLogs")
@Table(name = "passwordChangeLogs")
public class PasswordChangeLog {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "username")
    private String username;

    @Column(name = "oldPassword")
    private String oldPassword;

    @Column(name = "newPassword")
    private String newPassword;

    public PasswordChangeLog(LocalDateTime date, String username, String oldPassword, String newPassword) {
        this.date = date;
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
