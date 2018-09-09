package com.bockig.crazybackyard;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.bockig.crazybackyard.model.MetaData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackyardPicReceived extends S3FileReceiver {

    private static final Logger LOG = LogManager.getLogger(BackyardPicReceived.class);

    @Override
    protected void receiveObject(S3Object object, AmazonS3 s3Client) throws Exception {
        LOG.info("going to tweet");

        String file = object.getKey();
        String utc = object.getObjectMetadata().getUserMetaDataOf(MetaData.UTC);
        LOG.info("{} @ {}", file, utc);
    }
}
