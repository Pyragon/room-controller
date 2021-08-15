package com.cryo.entities;

import com.cryo.RoomController;
import com.cryo.controllers.LEDController;
import com.cryo.effects.Effect;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;

@Data
public class Scene extends MySQLDao {

	private final int id;
	private final String name;
	private final Timestamp timeToStart;
	private final String json;
	private final boolean active;
	private final Timestamp added;
	private final Timestamp updated;

	private ArrayList<Effect> effects;

	public void loadJSON() {
		EffectData[] effectData = RoomController.getGson().fromJson(json, EffectData[].class);

	}

	public void loop() {
		for(Effect effect : effects) {
			if (System.currentTimeMillis() > effect.getNextRun()) {
				int delay = effect.loop(LEDController.getStrip());
				effect.setNextRun(System.currentTimeMillis() + delay);
			}
		}
	}
}
