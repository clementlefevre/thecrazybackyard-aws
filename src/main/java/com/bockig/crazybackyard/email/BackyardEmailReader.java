package com.bockig.crazybackyard.email;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    List<Image> images() throws Exception {
        String contentType = message.getMimeMessage().getContentType();
        if (contentType.startsWith(MULTIPART_MIXED)) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getMimeMessage().getContent();
            return IntStream.range(0, mimeMultipart.getCount())
                    .mapToObj(i -> bodyPart(i, mimeMultipart))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(Image::isImage)
                    .map(Image::fromBodyPart)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Optional<BodyPart> bodyPart(Integer i, MimeMultipart mimeMultipart) {
        try {
            return Optional.of(mimeMultipart.getBodyPart(i));
        } catch (MessagingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    Optional<ZonedDateTime> timestamp() throws Exception {
        Enumeration h = message.getMimeMessage().getAllHeaders();
        String[] date = message.getMimeMessage().getHeader("Date");
        ZonedDateTime timestamp = null;
        if (date.length > 0) {
            timestamp = ZonedDateTime.parse(date[0].replaceAll("  ", ""), DateTimeFormatter.RFC_1123_DATE_TIME);
        }
        return Optional.ofNullable(timestamp);
    }
}
