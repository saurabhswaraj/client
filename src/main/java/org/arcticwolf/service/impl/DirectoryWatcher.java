package org.arcticwolf.service.impl;

import org.arcticwolf.service.Task;
import org.arcticwolf.service.Watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class DirectoryWatcher extends Watcher {
    private final String path;

    public DirectoryWatcher(String path) {
        this.path = path;
    }

    private boolean shouldWatch = true;

    @Override
    protected void watch() {

        try(WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path directoryPath = Paths.get(path);
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            shouldWatch = true;
            while(shouldWatch) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                    if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("File created: " + watchEvent.context());
                        Task task = new PropertiesFilter();
                        task.doWork(path, watchEvent.context().toString());
                    }
                }
                watchKey.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String getWatchedResource() {
        return path;
    }

    @Override
    protected void stopWatching() {
        shouldWatch = false;
    }

}
