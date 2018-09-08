package com.bockig.crazybackyard.email;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class S3CreatedRequestHandler implements RequestHandler<S3Event, String> {

    private static final Logger LOG = LogManager.getLogger(S3CreatedRequestHandler.class);

    public String handleRequest(S3Event s3event, Context context) {
        S3Util.logRecords(s3event.getRecords());
        AWSConfig config = AWSConfig.load();
        S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);

        String bucket = record.getS3().getBucket().getName();
        String key = S3Util.readKey(record);
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

        try (S3Object object = s3Client.getObject(bucket, key)) {
            Optional<BackyardEmailReader> reader = BackyardEmailReader.create(object.getObjectContent());
            if (reader.isPresent()) {
                BackyardEmailReader email = reader.get();
                Consumer<Image> writeImageToTargetBucket = i -> putImage(i, email, s3Client, config);
                LOG.info("received new email from '{}' with subject '{}'", email.sender(), email.subject());
                email.images().forEach(writeImageToTargetBucket);
                return "ok";
            } else {
                LOG.error("cannot read email!?");
            }
        } catch (Exception e) {
            LOG.error("error in handler", e);
        }
        return "error";
    }

    private void putImage(Image image, BackyardEmailReader email, AmazonS3 client, AWSConfig config) {
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
