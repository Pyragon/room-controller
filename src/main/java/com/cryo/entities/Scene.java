package com.cryo.entities;

import com.cryo.RoomController;
import com.cryo.controllers.StripController;
import com.cryo.controllers.SceneController;
import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Properties;

@Data
@EqualsAndHashCode(callSuper = false)
public class Scene extends MySQLDao {

	private Logger log = LoggerFactory.getLogger(Scene.class);

	@MySQLDefault
	private final int id;
	private final String name;
	private final Time timeToStart;
	private final String json;
	@MySQLDefault
	private final Timestamp added;
	@MySQLDefault
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
				effects.add(effectClass.getConstructor(int[].class, Properties.class).newInstance(data.getLeds(), data.getSettings()));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void loop(Ws281xLedStrip strip) {
		for(Effect effect : effects) {
			if (System.currentTimeMillis() > effect.getNextRun()) {
				int delay = effect.loop(strip);
				effect.setNextRun(System.currentTimeMillis() + delay);
			}
		}
	}
}
