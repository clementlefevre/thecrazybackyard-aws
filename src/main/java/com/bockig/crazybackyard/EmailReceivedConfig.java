package com.bockig.crazybackyard;

public class EmailReceivedConfig {
    private static final String TARGET_BUCKET = "CRAZYBACKYARD_S3_TARGET_BUCKET";

    private String targetBucket;

    private EmailReceivedConfig(String targetBucket) {
        this.targetBucket = targetBucket;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        if (targetBucket == null) {
            throw new RuntimeException("missing variable: " + TARGET_BUCKET);
        }
    }

    public static EmailReceivedConfig load() {
        return new EmailReceivedConfig(System.getenv(TARGET_BUCKET));
    }

    public String getTargetBucket() {
        return targetBucket;
    }
}
