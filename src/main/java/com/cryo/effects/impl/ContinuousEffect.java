package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class ContinuousEffect extends Effect {

	public ContinuousEffect(int[][] leds, Properties settings) {
		super(leds, settings);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		strip.setStrip(Color.BLACK);
		for(int i = 0; i < leds.length; i++) {
			for(int k = 0; k < leds[i].length; k++) {
				strip.setPixel(leds[i][k], Color.RED);
			}
		}
		log.info("Rendering continuous effect.");
		strip.render();
		return 60_000;
	}
}
