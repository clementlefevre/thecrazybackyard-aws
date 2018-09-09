package com.bockig.crazybackyard;

public class BackyardPicReceivedConfig {

    private static final String CONSUMER_KEY = "CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "CONSUMER_SECRET";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String ACCESS_SECRET = "ACCESS_SECRET";

    //twitter4j.oauth.consumerKey
//    twitter4j.oauth.consumerSecret
//    twitter4j.oauth.accessToken
//    twitter4j.oauth.accessTokenSecret

    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String accessSecret;

    private BackyardPicReceivedConfig(String consumerKey, String consumerSecret, String accessToken, String accessSecret) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        if (consumerSecret == null) {
            throw new RuntimeException("missing variable: " + CONSUMER_SECRET);
        }
        if (consumerKey == null) {
            throw new RuntimeException("missing variable: " + CONSUMER_KEY);
        }
        if (accessToken == null) {
            throw new RuntimeException("missing variable: " + ACCESS_TOKEN);
        }
        if (accessSecret == null) {
            throw new RuntimeException("missing variable: " + ACCESS_SECRET);
        }
    }

    public static BackyardPicReceivedConfig load() {
        return new BackyardPicReceivedConfig(System.getenv(CONSUMER_KEY), System.getenv(CONSUMER_SECRET), System.getenv(ACCESS_TOKEN), System.getenv(ACCESS_SECRET));
    }

}
