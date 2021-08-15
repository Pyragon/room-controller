package com.cryo.controllers;

import com.cryo.effects.Effect;
import com.cryo.effects.impl.RGBLoopEffect;
import com.cryo.entities.Scene;
import com.cryo.entities.annotations.ServerStart;
import com.cryo.entities.annotations.ServerStartSubscriber;
import com.cryo.utils.Utilities;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import static com.cryo.RoomController.getConnection;

@Slf4j
@ServerStartSubscriber
@Data
public class SceneController {

	@Getter
	private static HashMap<String, Class<Effect>> effects;
	private static HashMap<String, Scene> scenes;
	private static ArrayList<Scene> activeScenes;

	private final LEDController controller;

	@ServerStart
	public static void load() {
		try {
			effects = new HashMap<>();
			scenes = new HashMap<>();
			activeScenes = new ArrayList<>();
			for(Class<?> clazz : Utilities.getClasses("com.cryo.effects.impl")) {
				if(!clazz.getSimpleName().contains("Effect")) continue;
				if(!Effect.class.isAssignableFrom(clazz)) continue;
				Class<Effect> effectClass = (Class<Effect>) clazz;
				effects.put(effectClass.getSimpleName(), effectClass);
			}
			ArrayList<Scene> scenes = getConnection().selectList("scenes", Scene.class);
			for(Scene scene : scenes) {
				scene.loadJSON();
				SceneController.scenes.put(scene.getName(), scene);
				if(scene.isActive())
					activeScenes.add(scene);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void loop() {
		for(Scene scene : activeScenes)
			scene.loop();
		controller.render();
	}

}
