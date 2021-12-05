package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class ChristmasEffect extends Effect {

	private static final int WALK_DELAY = 50;
	private static final int WALK_COUNT = 10;

	private static final int PIXEL_OFFSET = 0;
	private static int COLOR_RANGE = 5;

	private static final int MAX_BRIGHTNESS = 50;
	private static final int LED_BRIGHTNESS = MAX_BRIGHTNESS;

	private int currentColour;
	private int currentStep;

	private final Color[] stripColour;

	public ChristmasEffect(int[] leds, Properties settings) {
		super(leds, settings);
		currentColour = COLOR_RANGE;
		stripColour = new Color[leds.length + 6];
		setStripColorsRGYW();
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		for(int i = 0; i < leds.length; i++) {
			Color pixelColour;
			if((i % (COLOR_RANGE + 1)) == currentColour)
				pixelColour = stripColour[(i + PIXEL_OFFSET)];
			else
				pixelColour = Color.BLACK;
			strip.setPixel(i, pixelColour);
		}
		strip.render();
		currentColour--;
		currentStep++;
		if(currentStep >= WALK_COUNT)
			currentStep = 0;
		if(currentColour < 0)
			currentColour = COLOR_RANGE;
		return WALK_DELAY;
	}

	void setStripColorsRGYW() {
		COLOR_RANGE = 5;

		int r = LED_BRIGHTNESS;
		int g = LED_BRIGHTNESS;
		int y = (LED_BRIGHTNESS * 215 / 255);
		int b = LED_BRIGHTNESS / 3;

		for (int i = 0; i < leds.length ; i += 6) {
			stripColour[i] =  new Color(r, 0, 0);
			stripColour[i+1] = new Color(0, g, 0);
			stripColour[i+2] = new Color(r, y, 0);
			stripColour[i+3] = new Color(r, 0, 0);
			stripColour[i+4] = new Color(0, g, 0);
			stripColour[i+5] = new Color(r, g, b);
		}
	}
}
