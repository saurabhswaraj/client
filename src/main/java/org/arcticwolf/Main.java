package org.arcticwolf;


import org.arcticwolf.config.ConfigProperties;
import org.arcticwolf.service.Watcher;
import org.arcticwolf.service.impl.DirectoryWatcher;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        String[] pathToWatch = ConfigProperties.config.get("directory.path").split(",");
        for(String path : pathToWatch) {
            Watcher watcher = new DirectoryWatcher(path);
            Thread thread = new Thread(watcher);
            thread.start();
            System.out.println(path);
        }

    }
}