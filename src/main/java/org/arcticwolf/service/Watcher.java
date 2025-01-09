package org.arcticwolf.service;

public interface Watcher extends Runnable {

    public void watch();

    public String getWatchedResource();

    public void stopWatching();
}
