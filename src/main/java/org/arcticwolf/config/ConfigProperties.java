package org.arcticwolf.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigProperties {
    public static final Map<String, String> config= new HashMap<>();
    static {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/config/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        prop.forEach((key, val) -> {
            config.put(key.toString(), val.toString());
        });
    }
}
