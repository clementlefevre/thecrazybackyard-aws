package com.bockig.crazybackyard.email;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class BackyardEmailReaderTest {

    @Test
    public void test() throws Exception {
        try (InputStream source = new FileInputStream("src/test/resources/mime/email-without-attachment")) {
            Optional<BackyardEmailReader> reader = BackyardEmailReader.create(source);
            ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 4, 23, 1, 0, 0, ZoneId.of("Europe/Berlin"));
            Assert.assertTrue(reader.isPresent());
            BackyardEmailReader readerObj = reader.get();

            Assert.assertEquals("axel@fake.com", readerObj.sender());
            Assert.assertEquals("test e-mail", readerObj.subject());
            Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
            Assert.assertEquals(0, readerObj.images().size());
        }
    }

    @Test
    public void testAttachment() throws Exception {
        try (InputStream source = new FileInputStream("src/test/resources/mime/email-with-attachment")) {
            Optional<BackyardEmailReader> reader = BackyardEmailReader.create(source);
            ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 2, 14, 8, 18, 0, ZoneId.of("Europe/Berlin"));

            Assert.assertTrue(reader.isPresent());
            BackyardEmailReader readerObj = reader.get();

            Assert.assertEquals("lappen@fake.com", readerObj.sender());
            Assert.assertEquals("Fwd: 4849-SYEW0124.JPG", readerObj.subject());
            Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
            List<Image> images = readerObj.images();
            Assert.assertEquals(1, images.size());
            Assert.assertEquals("SYEW0124.JPG", images.get(0).getFilename());        }
    }

    @Test
    public void testAttachmentReal() throws Exception {
        try (InputStream source = new FileInputStream("src/test/resources/mime/q6k3f98015ka9d24kalp1ffi8mifrnfr4d2tfq81")) {
            Optional<BackyardEmailReader> reader = BackyardEmailReader.create(source);
            ZonedDateTime timestamp = ZonedDateTime.of(2018, 9, 6, 19, 29, 46, 0, ZoneId.of("Europe/Berlin"));
            Assert.assertTrue(reader.isPresent());
            BackyardEmailReader readerObj = reader.get();

            Assert.assertEquals("sender@wildcam.com", readerObj.sender());
            Assert.assertEquals("4849-SYEW0144.JPG", readerObj.subject());
            Assert.assertTrue(timestamp.isEqual(readerObj.timestamp().get()));
            List<Image> images = readerObj.images();
            Assert.assertEquals(1, images.size());
            Assert.assertEquals("SYEW0144.JPG", images.get(0).getFilename());
        }
    }

}
