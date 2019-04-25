package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;
import com.github.mbelling.ws281x.Ws281xLedStrip;

public class ContinousEffect extends Effect {

    public ContinousEffect(Ws281xLedStrip strip, int[][] leds) {
         super(strip, leds);
    }
    @Override
    public void start() {
        for(int i = 0; i < leds.length; i++)
            for(int k = 0; k < leds[i].length; k++)
                getStrip().setPixel(leds[i][k], 255, 0, 0);
            getStrip().render();
    }

    @Override
    public void loop() {

    }
}
