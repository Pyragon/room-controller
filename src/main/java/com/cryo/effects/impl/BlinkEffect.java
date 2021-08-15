package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class BlinkEffect extends Effect {

	private boolean on;

	public BlinkEffect(int[] leds) {
		super(1000, leds); //any day, any hour, any minute, only on second 0 (once a minute), any milliseconds
	}

	@Override
	public void loop(Ws281xLedStrip strip) {

		log.info("Leds: "+ Arrays.toString(leds));

		for(int i = 0; i < leds.length; i++) {
			log.info("Setting "+leds[i]+" to: "+(on ? Color.BLACK : Color.RED));
			strip.setPixel(leds[i], on ? Color.BLACK : Color.RED);
		}
		log.info("Set strip to: "+on);
		on = !on;

	}
}
