package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.util.Properties;

public class BlinkEffect extends Effect {

    private int tick;
    private boolean lit;

    public BlinkEffect(Ws281xLedStrip strip, int ledStart, int ledEnd, Properties settings) {
        super(strip, ledStart, ledEnd, settings);
    }

    public BlinkEffect(Ws281xLedStrip strip, int[][] leds, Properties settings) {
        super(strip, leds, settings);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        //runs every 10ms
        //we want to blink every 500ms, so we'll add a number until we get to 50
        tick++;
        if(tick == 50) {
            tick = 0;
            if(lit) setStrip(new Color(0, 0, 0));
            else setStrip((Color) settings.get("colour"));
            getStrip().render();
            lit = !lit;
        }
    }

    public void setStrip(Color color) {
        for(int i = 0; i < leds.length; i++)
            for(int k = 0; k < leds[i].length; k++)
                getStrip().setPixel(leds[i][k], color);
    }
}
