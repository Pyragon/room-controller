package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class PoliceEffect extends Effect {

    private int stage;

    public PoliceEffect(Ws281xLedStrip strip, int[][] leds, Properties settings) {
        super(strip, leds, settings);
    }

    //default to 20 leds
    @Override
    public void start() {

    }

    @Override
    public void loop() {
    }
}
