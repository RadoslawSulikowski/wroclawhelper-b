package com.wroclawhelperb.service.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Mail {
    private String sendTo;
    private String subject;
    private String message;

}
