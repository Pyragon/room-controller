package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class ContinousEffect extends Effect {

    public ContinousEffect(Ws281xLedStrip strip, int[][] leds, Properties settings) {
         super(strip, leds, settings);
    }
    @Override
    public void start() {
        for(int i = 0; i < leds.length; i++)
            for(int k = 0; k < leds[i].length; k++)
                getStrip().setPixel(leds[i][k], (Color) settings.get("colour"));
            getStrip().render();
    }

    @Override
    public void loop() {

    }
}
