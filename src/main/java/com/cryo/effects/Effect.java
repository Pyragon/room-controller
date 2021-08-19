package com.cryo.effects;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;

import java.util.Properties;

@Data
public abstract class Effect {

	private long nextRun;
	protected final int[] leds;
	protected final Properties settings;

	public abstract int loop(Ws281xLedStrip strip);


}
