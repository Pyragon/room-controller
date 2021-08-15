package com.cryo.tasks;

import com.cryo.entities.Task;
import com.cryo.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;

/**
 * @author Cody Thompson <eldo.imo.rs@hotmail.com>
 *
 *         Created on: March 13, 2017 at 2:56:11 PM
 */
public class TaskManager extends TimerTask {

	private static ArrayList<Task> tasks;

	public TaskManager() {
		load();
	}

	public void load() {
		tasks = new ArrayList<>();
		try {
			for (Class<?> c : Utilities.getClasses("com.cryo.tasks.impl")) {
				if (c.isAnonymousClass())
					continue;
				if (c.getSimpleName().equals("Task"))
					continue;
				Task task = (Task) c.getConstructor().newInstance();
				task.setNextRun(System.currentTimeMillis() + 5000 + task.getDelay()); //add a 5s delay at beginning just in case. Make sure everything is setup first. Shouldn't be restarting often anyway.
				tasks.add(task);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void run() {
		for(Task task : tasks) {
			if(System.currentTimeMillis() >= task.getNextRun()) {
				task.run();
				task.setNextRun(System.currentTimeMillis() + task.getDelay());
			}
		}
	}

}
