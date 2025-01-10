package org.arcticwolf.service.impl;

import static org.arcticwolf.config.ConfigProperties.config;

import org.arcticwolf.model.CreatePropertiesRequest;
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
        String fileName = "";
        String pathToFileCreated;
        if (input.length == 2) {
            pathToFileCreated = input[0] + "/" + input[1];
            fileName = input[1];
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

        CreatePropertiesRequest createPropertiesRequest = new CreatePropertiesRequest();
        createPropertiesRequest.setProperties(properties);
        createPropertiesRequest.setFileName(fileName);

        boolean sendSuccess = sendToServer(createPropertiesRequest);
        if (!sendSuccess) {
            System.out.println("Failed to send properties to server");
            return;
        }
        boolean deleted = deleteFile(pathToFileCreated);
        System.out.println("Deleted : "+deleted);
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

    private boolean sendToServer(CreatePropertiesRequest createPropertiesRequest) {
        String server = config.get("server.address");
        Integer port = Integer.parseInt(config.get("server.port"));
        try(
                Socket socket = new Socket(server, port);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ) {
            out.writeObject(createPropertiesRequest);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean deleteFile(String path) {

        System.out.println("Thread "+Thread.currentThread().getName()+" deleting "+path);
        File file = new File(path);
        return file.delete();
    }



}
