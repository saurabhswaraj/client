package org.arcticwolf;


import org.arcticwolf.service.Watcher;
import org.arcticwolf.service.impl.DirectoryWatcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/config/config.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] pathToWatch = prop.getProperty("directory.path").split(",");
        for(String path : pathToWatch) {
            Watcher watcher = new DirectoryWatcher(path);
            Thread thread = new Thread(watcher);
            thread.start();
            System.out.println(path);
        }

    }
}