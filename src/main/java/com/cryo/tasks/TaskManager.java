package com.cryo.tasks;

import com.cryo.tasks.impl.SceneLoopTask;

import java.util.Timer;

public class TaskManager {

    private Timer timer;

    public void init() {
        timer = new Timer();
        timer.schedule(new SceneLoopTask(), 2000, 10);
    }
}
