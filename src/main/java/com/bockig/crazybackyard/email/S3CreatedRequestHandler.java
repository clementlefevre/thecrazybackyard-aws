package com.bockig.crazybackyard.email;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URLDecoder;

public class S3CreatedRequestHandler implements RequestHandler<S3Event, String> {

    private static final Logger LOG = LogManager.getLogger(S3CreatedRequestHandler.class);

    public String handleRequest(S3Event s3event, Context context) {

        LOG.info("moin!!");
        try {
            S3EventNotification.S3EventNotificationRecord record = s3event.getRecords().get(0);

            // Retrieve the bucket & key for the uploaded S3 object that
            // caused this Lambda function to be triggered
            String bkt = record.getS3().getBucket().getName();
            String key = record.getS3().getObject().getKey().replace('+', ' ');
            key = URLDecoder.decode(key, "UTF-8");

            // Read the source file as text
            AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();
            String body = s3Client.getObjectAsString(bkt, key);
            LOG.info("Body: {}", body);
            return "ok";
        } catch (Exception e) {
            LOG.error("error in handler", e);
            return "error";
        }
    }
}
