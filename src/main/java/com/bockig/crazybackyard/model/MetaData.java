package com.bockig.crazybackyard.model;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class MetaData {

    private static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    static final String UTC = "utc";
    static final String FROM = "from";
    static final String SUBJECT = "subject";

    private MetaData() {
    }

    public static String buildStatusText(Map<String, String> userMetadata) throws Exception {
        String utc = userMetadata.get(UTC);
        if (utc == null) {
            throw new Exception("missing metadata! not posting tweet " + userMetadata);
        }
        ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.valueOf(utc)), ZoneId.of("Europe/Berlin"));
        return String.format("Gotcha buddy! (%s) #trailcam", time.format(TIME_FORMAT));
    }
}
