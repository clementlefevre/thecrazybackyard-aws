package com.bockig.crazybackyard.email;

import org.apache.commons.mail.util.MimeMessageParser;
import org.junit.Assert;
import org.junit.Test;

import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class HelloWorld {

    @Test
    public void test() throws Exception {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);


        try (InputStream source = new FileInputStream("src/test/resources/mime/email-without-attachment")){

        }
        MimeMessage message = new MimeMessage(mailSession, source);

        MimeMessageParser p = new MimeMessageParser(message);

        assert "axel@fake.com".equals(p.getFrom());
        assert "test e-mail".equals(p.getSubject());
        List<DataSource> x = p.getAttachmentList();
        System.out.println(x);
    }

    @Test
    public void test2() throws Exception {
        Properties props = new Properties();
        Session mailSession = Session.getDefaultInstance(props, null);
        InputStream source = new FileInputStream("src/test/resources/mime/o1882sk1sfr94njra67is25cde5d7uiim0mbt001");
        MimeMessage message = new MimeMessage(mailSession, source);

        MimeMessageParser p = new MimeMessageParser(message);

        Assert.assertEquals("axel@bockig.com", p.getFrom());

        if (message.getContentType().contains("multipart")) {

            MimeMultipart x = (MimeMultipart) message.getContent();
            for (int i = 0; i < x.getCount(); i++) {
                BodyPart part = x.getBodyPart(i);
                if (part.isMimeType("text/plain")) {
                    System.out.println(part.getContent());
                    System.out.println("");
                }
            }
        }
//        Object message2 = p.getMimeMessage().getContent();
//


//        assert "test e-mail".equals(p.getSubject());
//        List<DataSource> x = p.getAttachmentList();
//        System.out.println(x);
    }
}
