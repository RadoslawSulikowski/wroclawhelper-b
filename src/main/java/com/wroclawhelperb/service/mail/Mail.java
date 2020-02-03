package com.wroclawhelperb.service.mail;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class Mail {
    private final String sendTo;
    private final String subject;
    private final String message;

    public static class MailBuilder {
        private String sendTo;
        private String subject;
        private List<String> messageLines = new ArrayList<>();

        public MailBuilder sendTo(String sendTo) {
            this.sendTo = sendTo;
            return this;
        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder messageLine(String line) {
            messageLines.add(line);
            return this;
        }

        public Mail build() {
            String message = String.join("\n", messageLines);
            return new Mail(sendTo, subject, message);
        }
    }

    private Mail(final String sendTo, final String subject, String message) {
        this.sendTo = sendTo;
        this.subject = subject;
        this.message = message;
    }
}
