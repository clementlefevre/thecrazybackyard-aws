package com.bockig.crazybackyard.model;

import com.amazonaws.services.s3.event.S3EventNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

public class S3Util {

    private static final Logger LOG = LogManager.getLogger(S3Util.class);

    private S3Util() {
    }

    public static String readKey(S3EventNotification.S3EventNotificationRecord record) {
        String key = record.getS3().getObject().getKey().replace('+', ' ');
        try {
            return URLDecoder.decode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("error decoding obj key", e);
            return key;
        }
    }

    public static void logRecords(List<S3EventNotification.S3EventNotificationRecord> records) {
        records.forEach(r -> LOG.info("found record: {}, {}, {}", r.getEventName(), r.getS3().getBucket(), r.getS3().getObject().getKey()));
    }
}
