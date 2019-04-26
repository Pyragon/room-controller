package com.cryo.entities;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;

import java.util.Properties;

@Data
public abstract class Effect {

    protected final Ws281xLedStrip strip;

    protected final int[][] leds;

    protected final Properties settings;

    public abstract void start();

    public abstract void loop();

}
