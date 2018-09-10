package com.bockig.crazybackyard.model;

import java.util.List;

public abstract class Config {

    private List<SystemProperty> properties;

    public Config(List<SystemProperty> properties) {
        this.properties = properties;
        failIfIncomplete();
    }

    private void failIfIncomplete() {
        properties.forEach(SystemProperty::failIfMissing);
    }

    protected String propertyValue(String key) {
        return properties.stream()
                .filter(p -> p.getKey().equals(key))
                .findAny()
                .orElseThrow(IllegalStateException::new)
                .getValue();
    }

}
