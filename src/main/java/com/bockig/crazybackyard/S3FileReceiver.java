package com.bockig.crazybackyard;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.S3Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class S3FileReceiver implements RequestHandler<S3Event, String> {

    private static final Logger LOG = LogManager.getLogger(S3FileReceiver.class);

    public String handleRequest(S3Event s3event, Context context) {
        S3Util.logRecords(s3event.getRecords());
        S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);

        String bucket = record.getS3().getBucket().getName();
        String key = S3Util.readKey(record);
        AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

        try (S3Object object = s3Client.getObject(bucket, key)) {
            receiveObject(object, s3Client);
        } catch (Exception e) {
            LOG.error("error in handler", e);
        }
        return "error";
    }

    protected abstract void receiveObject(S3Object object, AmazonS3 s3Client) throws Exception;

}
