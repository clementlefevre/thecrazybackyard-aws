package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.MetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import twitter4j.*;

import java.io.IOException;
import java.io.InputStream;

public class BackyardPicReceived extends S3FileReceiver {

    private static final Logger LOG = LogManager.getLogger(BackyardPicReceived.class);

    @Override
    protected void receiveObject(S3Object object, AmazonS3 s3Client) {
        LOG.info("going to tweet: {}", object.getKey());
        BackyardPicReceivedConfig config = BackyardPicReceivedConfig.load();
        Twitter twitter = new TwitterFactory(config.twitterConfig()).getInstance();

        try (InputStream fis = object.getObjectContent()) {
            String text = MetaData.buildStatusText(object.getObjectMetadata().getUserMetadata());
            postStatusUpdate(twitter, text, object.getKey(), fis);
        } catch (IOException e) {
            LOG.error("cannot get s3 content", e);
        } catch (TwitterException e) {
            LOG.error("twitter api error", e);
        } catch (Exception e) {
            LOG.error("something went wrong :(", e);
        }
    }

    private void postStatusUpdate(Twitter twitter, String text, String filename, InputStream fis) throws TwitterException {
        StatusUpdate update = new StatusUpdate(text);
        update.setMedia(filename, fis);
        Status result = twitter.updateStatus(update);
        LOG.info("successfully posted tweed with id {}", result.getId());
    }
}
