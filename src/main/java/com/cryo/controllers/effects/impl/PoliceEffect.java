package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;
import com.github.mbelling.ws281x.Ws281xLedStrip;

public class PoliceEffect extends Effect {

    private int stage;

    public PoliceEffect(Ws281xLedStrip strip, int[][] leds) {
        super(strip, leds);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
    }
}
