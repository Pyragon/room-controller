package com.cryo.effects;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;

@Data
public abstract class Effect {

	private long nextRun;

	protected final int[][] leds;

	public abstract int loop(Ws281xLedStrip strip);


}
