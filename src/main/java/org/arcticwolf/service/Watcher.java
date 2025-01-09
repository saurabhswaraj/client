package org.arcticwolf.service;

public abstract class Watcher implements Runnable {

    protected abstract void watch();

    protected abstract String getWatchedResource();

    protected abstract void stopWatching();

    @Override
    public void run() {
        watch();
    }
}
