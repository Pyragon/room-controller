package com.cryo.entities;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;

@Data
public abstract class Effect {

    protected final Ws281xLedStrip strip;

    protected final int[][] leds;

    public abstract void start();

    public abstract void loop();

}
