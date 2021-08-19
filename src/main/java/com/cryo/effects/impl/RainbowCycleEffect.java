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
		for(int i = 0; i < leds.length; i++) {
			byte[] wheel = wheel((byte) (((leds[i] * 256 / leds.length) + step) & 255));
			strip.setPixel(leds[i], new Color(wheel[0], wheel[1], wheel[2]));
		}
		strip.render();
		if(step++ >= MAX_STEPS)
			step = 0;
		return 20;
	}

	private byte[] wheel(byte pos) {
		byte[] wheel = new byte[3];
		if(pos < 85) {
			wheel[0] = (byte) (pos * 3);
			wheel[1] = (byte) (255 - pos * 3);
		} else if(pos < 170) {
			pos -= 85;
			wheel[0] = (byte) (255 - pos * 3);
			wheel[2] = (byte) (pos * 3);
		} else {
			pos -= 170;
			wheel[1] = (byte) (pos * 3);
			wheel[2] = (byte) (255 - pos * 3);
		}
		return wheel;
	}
}
