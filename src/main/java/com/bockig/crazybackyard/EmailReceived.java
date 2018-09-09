package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.BackyardEmailReader;
import com.bockig.crazybackyard.model.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class EmailReceived extends S3FileReceiver {

    private static final Logger LOG = LogManager.getLogger(EmailReceived.class);

    @Override
    protected void receiveObject(S3Object object, AmazonS3 s3Client) throws Exception {
        EmailReceivedConfig config = EmailReceivedConfig.load();
        Optional<BackyardEmailReader> reader = BackyardEmailReader.create(object.getObjectContent());
        if (reader.isPresent()) {
            BackyardEmailReader email = reader.get();
            Consumer<Image> writeImageToTargetBucket = i -> putImage(i, email, s3Client, config);
            LOG.info("received new email from '{}' with subject '{}'", email.sender(), email.subject());
            email.images().forEach(writeImageToTargetBucket);
        }
    }

    private void putImage(Image image, BackyardEmailReader email, AmazonS3 client, EmailReceivedConfig config) {
        try (InputStream is = image.inputStream()) {
            client.putObject(config.getTargetBucket(), image.getFilename(), is, meta(email.metaData()));
        } catch (IOException e) {
            LOG.error("cannot write image {}", image.getFilename(), e);
        }
    }

    private ObjectMetadata meta(Map<String, String> meta) {
        ObjectMetadata metaData = new ObjectMetadata();
        meta.forEach(metaData::addUserMetadata);
        return metaData;
    }
}
