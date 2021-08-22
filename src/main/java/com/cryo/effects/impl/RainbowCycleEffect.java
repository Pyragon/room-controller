package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class RainbowCycleEffect extends Effect {

	public static int MAX_STEPS = (256*5);

	private int step;

	public RainbowCycleEffect(int[] leds, Properties settings) {
		super(leds, settings);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		for (int led : leds) {
			int[] wheel = wheel((((led * 256 / leds.length) + step) & 255));
			strip.setPixel(led, new Color(wheel[0], wheel[1], wheel[2]));
		}
		strip.render();
		if(step++ >= MAX_STEPS)
			step = 0;
		return 20;
	}

	private int[] wheel(int pos) {
		int[] wheel = new int[3];
		if(pos < 85) {
			wheel[0] = (pos * 3);
			wheel[1] = (255 - pos * 3);
		} else if(pos < 170) {
			pos -= 85;
			wheel[0] = (255 - pos * 3);
			wheel[2] = (pos * 3);
		} else {
			pos -= 170;
			wheel[1] = (pos * 3);
			wheel[2] = (255 - pos * 3);
		}
		return wheel;
	}
}
