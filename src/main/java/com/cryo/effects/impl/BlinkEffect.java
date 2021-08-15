package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

public class BlinkEffect extends Effect {

	private boolean on;

	public BlinkEffect(int[] leds) {
		super("* * * 0 *", leds); //any day, any hour, any minute, only on second 0 (once a minute), any milliseconds
	}

	@Override
	public void loop(Ws281xLedStrip strip) {

		for(int i = 0; i < leds.length; i++)
			strip.setPixel(i, on ? Color.RED : Color.BLACK);
		on = !on;

	}
}
