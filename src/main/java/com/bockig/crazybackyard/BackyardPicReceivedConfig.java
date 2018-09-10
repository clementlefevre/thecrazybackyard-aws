package com.bockig.crazybackyard;

import com.bockig.crazybackyard.model.Config;
import com.bockig.crazybackyard.model.SystemProperty;

import java.util.ArrayList;
import java.util.List;

public class BackyardPicReceivedConfig extends Config {

    private static final String CONSUMER_KEY = "CRAZYBACKYARD_TWITTER_CONSUMER_KEY";
    private static final String CONSUMER_SECRET = "CRAZYBACKYARD_TWITTER_CONSUMER_SECRET";
    private static final String ACCESS_TOKEN = "CRAZYBACKYARD_TWITTER_ACCESS_TOKEN";
    private static final String ACCESS_SECRET = "CRAZYBACKYARD_TWITTER_ACCESS_SECRET";

    private BackyardPicReceivedConfig(List<SystemProperty> properties) {
        super(properties);
    }

    public static BackyardPicReceivedConfig load() {
        return new BackyardPicReceivedConfig(new ArrayList<>(SystemProperty.create(CONSUMER_KEY, CONSUMER_SECRET, ACCESS_TOKEN, ACCESS_SECRET)));
    }

}
