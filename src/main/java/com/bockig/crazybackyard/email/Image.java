package com.bockig.crazybackyard.email;

import org.apache.commons.io.IOUtils;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

class Image {

    private static final String MULTIPART_MIXED = "multipart/mixed";

    private final String filename;
    private final byte[] bytes;

    Image(String filename, byte[] bytes) {
        this.filename = filename;
        this.bytes = bytes;
    }

    static Optional<Image> fromBodyPart(BodyPart bodyPart) {
        try (InputStream is = (InputStream) bodyPart.getContent()) {

            byte[] bytes = IOUtils.toByteArray(is);
            String filename = bodyPart.getFileName();
            return Optional.of(new Image(filename, bytes));
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    static boolean isImage(BodyPart bodyPart) {
        //text/plain; format=flowed; charset="UTF-8"
        //image/jpeg; name="SYEW0124.JPG"
        try {
            return bodyPart.getContentType().startsWith(IMAGE_JPEG);
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
