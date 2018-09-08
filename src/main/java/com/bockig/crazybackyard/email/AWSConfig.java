package com.bockig.crazybackyard.email;

class AWSConfig {
    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";

    private String targetBucket;

    private AWSConfig(String targetBucket) {
        this.targetBucket = targetBucket;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        if (targetBucket == null) {
            throw new RuntimeException("missing variable: " + TARGET_BUCKET);
        }
    }

    static AWSConfig load() {
        return new AWSConfig(System.getenv(TARGET_BUCKET));
    }

    String getTargetBucket() {
        return targetBucket;
    }
}
