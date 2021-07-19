package com.google.firebase.udacity.moviehub;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {
    // This is for all the background task
    // Singleton Pattern
    private static AppExecutors instance;
    public static AppExecutors getInstance(){
        if (instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3); // for background thread
    public ScheduledExecutorService getNetworkIO(){
        return mNetworkIO;
    }
}
