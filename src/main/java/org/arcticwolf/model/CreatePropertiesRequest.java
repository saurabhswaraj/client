package org.arcticwolf.model;

import java.io.Serializable;
import java.util.Map;

public class CreatePropertiesRequest implements Serializable {
    private String fileName;
    private Map<String, String> properties;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
    public Map<String, String> getProperties() {
        return properties;
    }
}
