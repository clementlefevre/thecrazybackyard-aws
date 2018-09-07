package com.bockig.crazybackyard.email;

import org.apache.commons.lang3.StringUtils;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class EmailText {

    private static final String TEXT_PLAIN = "text/plain";
    private static DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Pic was taken on 06/09/2018 19:29:46 Battery:60%~80%
    private static final Pattern TAKEN_ON_TEXT = Pattern.compile("Pic was taken on (\\d\\d/\\d\\d/\\d\\d\\d\\d \\d\\d:\\d\\d:\\d\\d) ");

    private final String text;

    private EmailText(String text) {
        this.text = text;
    }

    static Optional<EmailText> fromBodyPart(BodyPart bodyPart) {
        if (!isText(bodyPart)) {
            return Optional.empty();
        }
        try {
            return Optional.of(new EmailText((String) bodyPart.getContent()));
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static boolean isText(BodyPart bodyPart) {
        try {
            return bodyPart.getContentType().startsWith(TEXT_PLAIN);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    Optional<ZonedDateTime> extractTimestampFromText() {
        Matcher matcher = TAKEN_ON_TEXT.matcher(StringUtils.trim(text));
        if (matcher.find()) {
            String dateStr = matcher.group(1);
            ZonedDateTime timestamp = ZonedDateTime.of(LocalDateTime.parse(dateStr, DATE_FORMAT), ZoneId.of("Europe/Berlin"));
            return Optional.of(timestamp);
        }
        return Optional.empty();
    }
}
