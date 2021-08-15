package com.cryo.tasks.impl;

import com.cryo.RoomController;
import com.cryo.entities.Task;

public class SceneLoopTask extends Task {

	public SceneLoopTask() {
		super(20);
	}

	@Override
	public void run() {
		RoomController.getINSTANCE().getSceneController().loop();
	}

}
