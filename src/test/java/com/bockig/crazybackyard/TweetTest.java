package com.bockig.crazybackyard;

import org.junit.Ignore;
import org.junit.Test;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TweetTest {

    @Test
    @Ignore
    public void test() throws Exception {
        Twitter twitter = TwitterFactory.getSingleton();
        File f = new File("D:\\downloads\\SYEW0148.JPG");
        StatusUpdate statuss = new StatusUpdate("test pic");
        try (InputStream fis = new FileInputStream(f)) {
            statuss.setMedia("SYEW0148.JPG", fis);
            Status status = twitter.updateStatus(statuss);
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        }
    }

}
