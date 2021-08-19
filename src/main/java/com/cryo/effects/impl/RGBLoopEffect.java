package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class RGBLoopEffect extends Effect {

	private int loopIndex;
	private boolean fading;
	private int colourIndex;

	public RGBLoopEffect(int[] leds, Properties properties) {
		super(leds, properties);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		int red = loopIndex == 0 ? colourIndex : 0;
		int green = loopIndex == 1 ? colourIndex : 0;
		int blue = loopIndex == 2 ? colourIndex : 0;
		for(int i = 0; i < leds.length; i++)
			strip.setPixel(i, new Color(red, green, blue));
		if(fading) {
			colourIndex--;
			if(colourIndex < 0) {
				colourIndex = 0;
				fading = false;
				loopIndex++;
				if(loopIndex == 3) loopIndex = 0;
			}
		} else {
			colourIndex++;
			if(colourIndex == 256) {
				fading = true;
				colourIndex = 255;
			}
		}
		strip.render();
		return 5;
	}
}
