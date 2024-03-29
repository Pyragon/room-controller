package com.cryo.effects.impl;

import com.cryo.RoomController;
import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ContinuousEffect extends Effect {

	public ContinuousEffect(int[] leds, Properties settings) {
		super(leds, settings);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		for(int i = 0; i < leds.length; i++)
			strip.setPixel(leds[i], RoomController.getGson().fromJson(settings.getProperty("colour"), Color.class));
		strip.render();
		return 10_000;
	}
}
