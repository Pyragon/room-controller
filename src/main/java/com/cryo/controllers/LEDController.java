package com.cryo.controllers;

import com.cryo.RoomController;
import com.cryo.entities.annotations.ServerStart;
import com.cryo.entities.annotations.ServerStartSubscriber;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ServerStartSubscriber
public class LEDController {

	@Getter
	private static Ws281xLedStrip strip;

	@ServerStart
	public static void load() {
		int ledsCount = Integer.parseInt(RoomController.getProperties().getProperty("leds_count"));
		//strip = new Ws281xLedStrip(ledsCount, 18, 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_GRB, false);
	}

	public void render() {
		strip.render();
	}
}
