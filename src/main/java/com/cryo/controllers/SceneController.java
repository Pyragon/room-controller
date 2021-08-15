package com.cryo.controllers;

import com.cryo.RoomController;
import com.cryo.effects.Effect;
import com.cryo.effects.impl.BlinkEffect;
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
		BlinkEffect blink = new BlinkEffect(IntStream.range(0, 10).toArray());
		blink.setNextRun(System.currentTimeMillis() + 5000 + blink.getDelay()); //Add 5s delay just in case.
		effects = new ArrayList<>() {{
			add(blink);
		}};
	}

	public void loop() {
		for(Effect effect : effects) {
			if(System.currentTimeMillis() > effect.getNextRun()) {
				effect.loop(LEDController.getStrip());
				effect.setNextRun(System.currentTimeMillis() + effect.getDelay());
			}
		}
		controller.render();
	}

}
