package com.wroclawhelperb.domain.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "EmailsSentStatistics")
@Table(name = "emailsSentStatistics")
public class EmailsSentStatistic {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, unique = true)
    private Long id;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "registeredUsers")
    private int registeredUsers;

    @Column(name = "emailsSent")
    private int emailsSent;

    public EmailsSentStatistic(LocalDateTime date, int registeredUsers, int emailsSent) {
        this.date = date;
        this.registeredUsers = registeredUsers;
        this.emailsSent = emailsSent;
    }
}
