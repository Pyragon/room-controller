package com.cryo.effects;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;

@Data
public abstract class Effect {

	private long nextRun;

	private final long delay;
	protected final int[] leds;

	public abstract void loop(Ws281xLedStrip strip);


}
