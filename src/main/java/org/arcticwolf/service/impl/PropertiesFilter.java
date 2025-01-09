package org.arcticwolf.service.impl;

import static org.arcticwolf.config.ConfigProperties.config;
import org.arcticwolf.service.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class PropertiesFilter implements Task {


    @Override
    public void doWork(String... input) {
        System.out.println("Came to doWork "+Thread.currentThread().getName());
        String pathToFileCreated;
        if (input.length == 1) {
            pathToFileCreated = input[0];
        } else {
            System.out.println("Input not suitable for this flow" +input.length);
            return;
        }
        if (!pathToFileCreated.endsWith(".properties")) {
            System.out.println("Input not a properties file" +pathToFileCreated);
            return;
        }

        Properties prop = new Properties();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try(FileInputStream fis = new FileInputStream(pathToFileCreated)) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Map<String, String> properties = convertPropertiesToMap(prop);
        System.out.println(properties);
        boolean deleted = deleteFile(pathToFileCreated);
        System.out.println("Deleted : "+deleted);
        sendToServer(properties);
    }

    private Map<String, String> convertPropertiesToMap(Properties props) {
        System.out.println("Came to convertPropertiesToMap "+Thread.currentThread().getName());
        Map<String, String> map = new HashMap<>();
        String regex = config.get("key.filter");
        for (String key : props.stringPropertyNames()) {
            System.out.println(key + " = " + props.getProperty(key));
            if(regex != null) {
                if(key.matches(regex)) {
                    System.out.println("key" + " = " + key);
                    map.put(key, props.getProperty(key));
                }
            }
        }
        return map;
    }

    private void sendToServer(Map<String, String> map) {
        String server = config.get("server.address");
        Integer port = Integer.parseInt(config.get("server.port"));
        try(
                Socket socket = new Socket(server, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ) {
            out.writeObject(map);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean deleteFile(String path) {

        System.out.println("Thread "+Thread.currentThread().getName()+" deleting "+path);
        File file = new File(path);
        return file.delete();
    }



}
