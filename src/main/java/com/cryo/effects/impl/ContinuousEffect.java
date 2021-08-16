package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

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
		strip.render();
		return 60_000;
	}
}
