package com.bockig.crazybackyard.model;

import java.util.ArrayList;
import java.util.List;

public class SystemProperty {

    private String key;
    private String value;

    private SystemProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static List<SystemProperty> create(String... keys) {
        List<SystemProperty> properties = new ArrayList<>();
        for (String key : keys) {
            properties.add(new SystemProperty(key, System.getenv(key)));
        }
        return properties;
    }

    void failIfMissing() {
        if (value == null) {
            throw new RuntimeException("missing variable: " + key);
        }
    }

    String getKey() {
        return key;
    }

    String getValue() {
        return value;
    }
}
