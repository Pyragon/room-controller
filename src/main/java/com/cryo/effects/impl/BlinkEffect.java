package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Properties;

@Slf4j
public class BlinkEffect extends Effect {

	private boolean on;

	public BlinkEffect(int[] leds, Properties properties) {
		super(leds, properties); //any day, any hour, any minute, only on second 0 (once a minute), any milliseconds
	}

	@Override
	public int loop(Ws281xLedStrip strip) {

		for(int i = 0; i < leds.length; i++)
			strip.setPixel(leds[i], on ? Color.BLACK : Color.RED);
		on = !on;
		return 1000;
	}
}
