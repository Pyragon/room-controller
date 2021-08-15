package com.cryo.controllers;

import com.cryo.RoomController;
import com.cryo.effects.Effect;
import com.cryo.effects.impl.BlinkEffect;
import com.cryo.effects.impl.MeteorEffect;
import com.cryo.entities.annotations.ServerStart;
import com.cryo.entities.annotations.ServerStartSubscriber;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

@Slf4j
@ServerStartSubscriber
@Data
public class SceneController {

	private static ArrayList<Effect> effects;

	private final LEDController controller;

	@ServerStart
	public static void load() {
		MeteorEffect meteor = new MeteorEffect(new int[][] { IntStream.range(0, 10).toArray() });
		meteor.setNextRun(System.currentTimeMillis() + 5000); //Add 5s delay just in case.
		effects = new ArrayList<>() {{
			add(meteor);
		}};
	}

	public void loop() {
		for(Effect effect : effects) {
			if(System.currentTimeMillis() > effect.getNextRun()) {
				int delay = effect.loop(LEDController.getStrip());
				effect.setNextRun(System.currentTimeMillis() + delay);
			}
		}
		controller.render();
	}

}
