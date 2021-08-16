package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.Random;

@Slf4j
public class MeteorEffect extends Effect {

	private final static boolean METEOR_RANDOM_DECAY = true;
	private final static byte METEOR_TRAIL_DECAY = 64;
	private final static int METEOR_SIZE = 10;
	private final static int METEOR_DELAY = 30;
	// meteorRain(0xff,0xff,0xff,10, 64, true, 30);

	private final static Color COLOR = new Color(255, 255, 255);

	private final Random random;

	private int ledIndex;

	public MeteorEffect(int[][] leds, Properties properties) {
		super(leds, properties);
		random = new Random();
	}

	@Override
	public int loop(Ws281xLedStrip strip) {

		strip.setStrip(Color.BLACK);

		int size = leds[ledIndex].length;
		for (int i = 0; i < size + size; i++) {

			for (int j = 0; j < size; j++) {

				if (!METEOR_RANDOM_DECAY || random.nextInt(10) > 5) {
					fadeToBlack(strip, j, METEOR_TRAIL_DECAY);
				}

			}

			for (int j = 0; j < METEOR_SIZE; j++) {
				if ((i - j < size) && (i - j) > 0) strip.setPixel(i - j, COLOR);
			}

		}
		log.info("rendering strip.");
		strip.render();
		ledIndex++;
		if (ledIndex >= leds.length) ledIndex = 0;
		return METEOR_DELAY;
	}

	public void fadeToBlack(Ws281xLedStrip strip, int pixel, byte value) {
		long oldColor;
		long r, g, b;

		oldColor = strip.getPixel(pixel);
		r = (oldColor & 0x00ff0000) >> 16;
		g = (oldColor & 0x0000ff00) >> 8;
		b = (oldColor & 0x000000ff);

		r = (r <= 10) ? 0 : (int) r - (r * value / 256);
		g = (g <= 10) ? 0 : (int) g - (g * value / 256);
		b = (b <= 10) ? 0 : (int) b - (b * value / 256);

		strip.setPixel(pixel, new Color((int) r, (int) g, (int) b));
	}
}
