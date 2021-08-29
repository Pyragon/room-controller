package com.cryo.controllers;

import com.cryo.RoomController;
import com.cryo.entities.Strip;
import com.cryo.entities.annotations.ServerStart;
import com.cryo.entities.annotations.ServerStartSubscriber;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class StripController {

	@Getter
	private HashMap<Integer, Strip> strips;

	public void load() {
		strips = new HashMap<>();
		ArrayList<Strip> strips = RoomController.getConnection().selectList("strips", "active=?", Strip.class, 1);
		for(Strip strip : strips) {
			strip.load();
			this.strips.put(strip.getId(), strip);
//			strip.setStrip(new Ws281xLedStrip(strip.getLedCount(), strip.getPin(), 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_GRB, true));
		}
	}

	public void loop() {
//		for(Strip strip : strips.values())
//			strip.loop();
	}
}
