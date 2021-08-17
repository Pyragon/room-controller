package com.cryo.controllers;

import com.cryo.effects.Effect;
import com.cryo.entities.Scene;
import com.cryo.entities.annotations.ServerStart;
import com.cryo.entities.annotations.ServerStartSubscriber;
import com.cryo.utils.Utilities;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

import static com.cryo.RoomController.getConnection;

@Slf4j
@Data
public class SceneController {

	@Getter
	private static HashMap<String, Class<Effect>> effects;
	@Getter
	private static HashMap<Integer, Scene> scenes;

	private final StripController controller;

	@SuppressWarnings("unchecked")
	public void load() {
		try {
			effects = new HashMap<>();
			scenes = new HashMap<>();
			for(Class<?> clazz : Utilities.getClasses("com.cryo.effects.impl")) {
				if(!clazz.getSimpleName().contains("Effect")) continue;
				if(!Effect.class.isAssignableFrom(clazz)) continue;
				Class<Effect> effectClass = (Class<Effect>) clazz;
				effects.put(effectClass.getSimpleName(), effectClass);
			}
			ArrayList<Scene> scenes = getConnection().selectList("scenes", Scene.class);
			for(Scene scene : scenes) {
				scene.loadJSON();
				SceneController.scenes.put(scene.getId(), scene);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
