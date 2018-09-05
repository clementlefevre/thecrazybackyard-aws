package com.bockig.crazybackyard.email;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

public class BackyardEmailReaderTest {

    @Test
    public void test() throws Exception {
        try (InputStream source = new FileInputStream("src/test/resources/mime/email-without-attachment")) {
            Optional<BackyardEmailReader> reader = BackyardEmailReader.create(source);
            ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 4, 23, 1, 0, 0, ZoneId.of("Europe/Berlin"));

            Assert.assertTrue(reader.isPresent());
            Assert.assertEquals("axel@fake.com", reader.get().sender());
            Assert.assertEquals("test e-mail", reader.get().subject());
            Assert.assertTrue(timestamp.isEqual(reader.get().timestamp().get()));
        }
    }

}
