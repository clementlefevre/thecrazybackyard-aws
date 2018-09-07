package com.bockig.crazybackyard.email;

import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BackyardEmailReader {

    private static final String MULTIPART_MIXED = "multipart/mixed";
    private static final String MULTIPART_ALTERNATIVE = "multipart/alternative";

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
                    .map(Image::fromBodyPart)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    List<EmailText> texts() {
        try {
            String contentType = message.getMimeMessage().getContentType();
            if (contentType.startsWith(MULTIPART_MIXED) || contentType.startsWith(MULTIPART_ALTERNATIVE)) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getMimeMessage().getContent();
                return IntStream.range(0, mimeMultipart.getCount())
                        .mapToObj(i -> bodyPart(i, mimeMultipart))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(EmailText::fromBodyPart)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Optional<BodyPart> bodyPart(Integer i, MimeMultipart mimeMultipart) {
        try {
            return Optional.of(mimeMultipart.getBodyPart(i));
        } catch (MessagingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    Optional<ZonedDateTime> timestamp() {
        Optional<ZonedDateTime> fromText = timestampFromText();
        if (fromText.isPresent()) {
            return fromText;
        }
        return fromEmailHeader();
    }

    private Optional<ZonedDateTime> fromEmailHeader() {
        try {
            String[] date = message.getMimeMessage().getHeader("Date");
            ZonedDateTime timestamp = null;
            if (date.length > 0) {
                timestamp = ZonedDateTime.parse(date[0].replaceAll("  ", ""), DateTimeFormatter.RFC_1123_DATE_TIME);
            }
            return Optional.ofNullable(timestamp);
        } catch (MessagingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<ZonedDateTime> timestampFromText() {
        return texts().stream()
                .map(EmailText::extractTimestampFromText)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findAny();
    }
}
