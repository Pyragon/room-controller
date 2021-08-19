package com.cryo.effects.impl;

import com.cryo.effects.Effect;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Properties;

public class PoliceEffect extends Effect {

	public PoliceEffect(int[] leds, Properties settings) {
		super(leds, settings);
	}

	@Override
	public int loop(Ws281xLedStrip strip) {
		//
		return 0;
	}
}
