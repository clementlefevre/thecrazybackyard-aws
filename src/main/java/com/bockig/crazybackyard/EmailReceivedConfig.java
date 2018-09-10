package com.bockig.crazybackyard;

import com.bockig.crazybackyard.model.Config;
import com.bockig.crazybackyard.model.SystemProperty;

import java.util.ArrayList;
import java.util.List;

class EmailReceivedConfig extends Config {

    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";

    EmailReceivedConfig(List<SystemProperty> properties) {
        super(properties);
    }

    static EmailReceivedConfig load() {
        return new EmailReceivedConfig(new ArrayList<>(SystemProperty.create(TARGET_BUCKET)));
    }

    String getTargetBucket() {
        return propertyValue(TARGET_BUCKET);
    }

}
