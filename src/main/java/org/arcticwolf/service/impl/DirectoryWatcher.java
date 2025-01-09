package org.arcticwolf.service.impl;

import org.arcticwolf.service.Watcher;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class DirectoryWatcher implements Watcher {
    private final String path;

    public DirectoryWatcher(String path) {
        this.path = path;
    }

    private boolean shouldWatch = true;

    @Override
    public void watch() {

        try(WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path directoryPath = Paths.get(path);
            directoryPath.register(watchService, StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            shouldWatch = true;
            while(shouldWatch) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {

                    if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        System.out.println("File created: " + watchEvent.context());
                    }
                    else if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                        System.out.println("File deleted: " + watchEvent.context());
                    }
                    else if (watchEvent.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        System.out.println("File modified: " + watchEvent.context());
                    }
                }
                watchKey.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getWatchedResource() {
        return path;
    }

    @Override
    public void stopWatching() {
        shouldWatch = false;
    }

    @Override
    public void run() {
        watch();
    }
}
