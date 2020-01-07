package com.cryo.tasks.impl;

import com.cryo.Main;

import java.util.TimerTask;

public class SceneLoopTask extends TimerTask {

    @Override
    public void run() {
        Main.getInstance().getSceneController().loop();
    }
}
