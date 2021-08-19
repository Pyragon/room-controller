package com.cryo.effects.impl;

import com.cryo.RoomController;
import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;
import java.util.Random;

public class HalloweenEyesEffect extends Effect {

	private final Random random;

	private final Color color;
	private final int eyeWidth;
	private final int eyeSpace;
	private final boolean fade;
	private int steps;
	private int fadeDelay;
	private int nextEndPause;

	private boolean fading;
	private int startIndex;
	private int startSecondIndex;
	private int step;

	public HalloweenEyesEffect(int[] leds, Properties settings) {
		super(leds, settings);
		random = new Random();
		color = RoomController.getGson().fromJson(settings.getProperty("colour"), Color.class);
		eyeWidth = 1;
		eyeSpace = 4;
		fade = true;
	}

	public int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}

	public int random(int maxValue) {
		if (maxValue <= 0) return 0;
		return random.nextInt(maxValue);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		if(fading) {

			int r = step * (color.getRed() / steps);
			int g = step * (color.getGreen() / steps);
			int b = step * (color.getBlue() / steps);

			for(int j = 0; j < eyeWidth; j++) {
				strip.setPixel(startIndex + j, new Color(r, g, b));
				strip.setPixel(startSecondIndex + j, new Color(r, g, b));
			}
			step--;
			if(step < 0)
				fading = false;
			return fadeDelay;
		}
		for(int i = 0; i < leds.length; i++)
			strip.setPixel(i, Color.BLACK);
		startIndex = random.nextInt(leds.length - (eyeWidth*2) - eyeSpace);
		startSecondIndex = startIndex + eyeWidth + eyeSpace;

		for(int i = 0; i < eyeWidth; i++) {
			strip.setPixel(leds[startIndex + i], color);
			strip.setPixel(leds[startSecondIndex + i], color);
		}

		strip.render();
		if(fade) {
			fading = true;
			step = steps;
			return 5;
		}
		steps = random(5, 50);
		fadeDelay = random(5, 150);
		int endPause = nextEndPause;
		nextEndPause = random(1000, 10_000);
		return endPause;
	}
}
