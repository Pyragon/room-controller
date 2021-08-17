package com.cryo.entities;

import com.cryo.RoomController;
import com.cryo.controllers.SceneController;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.util.HashMap;

@Data
@EqualsAndHashCode(callSuper = false)
public class Strip extends MySQLDao {

	@MySQLDefault
	private final int id;
	private final String name;
	private final int pin;
	private final int ledCount;
	@MySQLRead("scenes")
	private final String scenesString;
	private final boolean active;
	@MySQLDefault
	private final Timestamp added;
	@MySQLDefault
	private final Timestamp updated;

	private Ws281xLedStrip strip;

	private HashMap<Integer, Scene> activeScenes;

	public void load() {
		activeScenes = new HashMap<>();
		int[] scenes = RoomController.getGson().fromJson(scenesString, int[].class);
		for(int sceneId : scenes) {
			Scene scene = SceneController.getScenes().get(sceneId);
			if(scene == null) continue;
			activeScenes.put(sceneId, scene);
		}
	}

	public void loop() {
		for(Scene scene : activeScenes.values())
			scene.loop(strip);
	}
}
