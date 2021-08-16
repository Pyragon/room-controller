package com.cryo.entities;

import com.cryo.RoomController;
import com.cryo.controllers.LEDController;
import com.cryo.controllers.SceneController;
import com.cryo.effects.Effect;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

@Data
public class Scene extends MySQLDao {

	private Logger log = LoggerFactory.getLogger(Scene.class);

	private final int id;
	private final String name;
	private final Timestamp timeToStart;
	private final String json;
	private final boolean active;
	private final Timestamp added;
	private final Timestamp updated;

	private ArrayList<Effect> effects;

	public void loadJSON() {
		effects = new ArrayList<>();
		EffectData[] effectData = RoomController.getGson().fromJson(json, EffectData[].class);
		if(effectData == null) {
			log.error("Error loading effect data.");
			return;
		}
		for(EffectData data : effectData) {
			Class<Effect> effectClass = SceneController.getEffects().get(data.getEffectName());
			if(effectClass == null) continue;
			try {
				effects.add(effectClass.getConstructor(int[][].class, Properties.class).newInstance(data.getLeds(), data.getSettings()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
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
