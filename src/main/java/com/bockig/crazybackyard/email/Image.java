package com.bockig.crazybackyard.email;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class Image {

    private static final Logger LOG = LogManager.getLogger(Image.class);

    private static final String JPG_SUFFIX = ".jpg";

    private final String filename;
    private final byte[] bytes;

    private Image(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    static Optional<Image> fromBodyPart(BodyPart bodyPart) {
        if (!isImage(bodyPart)) {
            return Optional.empty();
        }
        try (InputStream is = (InputStream) bodyPart.getContent()) {

            byte[] bytes = IOUtils.toByteArray(is);
            String filename = bodyPart.getFileName();
            return Optional.of(new Image(filename, bytes));
        } catch (MessagingException | IOException e) {
            LOG.error("cannot create image from BodyPart", e);
        }
        return Optional.empty();
    }

    private static boolean isImage(BodyPart bodyPart) {
        try {
            String filename = bodyPart.getFileName();
            return StringUtils.isNotBlank(filename) && filename.toLowerCase().endsWith(JPG_SUFFIX);
        } catch (MessagingException e) {
            LOG.error("check test if image", e);
            return false;
        }
    }

    String getFilename() {
        return filename;
    }

    InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }
}
