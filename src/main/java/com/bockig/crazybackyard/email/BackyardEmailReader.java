package com.bockig.crazybackyard.email;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Optional;
import java.util.Properties;

class BackyardEmailReader {

    private MimeMessageParser message;

    private BackyardEmailReader(MimeMessageParser message) {
        this.message = message;
    }

    static Optional<BackyardEmailReader> create(InputStream source) {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        try {
            MimeMessage message = new MimeMessage(mailSession, source);
            return Optional.of(new BackyardEmailReader(new MimeMessageParser(message)));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    String sender() throws Exception {
        return message.getFrom();
    }

    String subject() throws Exception {
        return message.getSubject();
    }

    Optional<ZonedDateTime> timestamp() throws Exception {
        Enumeration h = message.getMimeMessage().getAllHeaders();
        String[] date = message.getMimeMessage().getHeader("Date");
        ZonedDateTime timestamp = null;
        if (date.length > 0) {
            timestamp = ZonedDateTime.parse(date[0], DateTimeFormatter.RFC_1123_DATE_TIME);
        }
        return Optional.ofNullable(timestamp);
    }
}
