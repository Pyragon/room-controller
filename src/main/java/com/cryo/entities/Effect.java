package com.cryo.entities;

import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.eclipse.jetty.util.ArrayUtil;

import java.util.Properties;

@Data
@RequiredArgsConstructor
public abstract class Effect {

    public Effect(Ws281xLedStrip strip, int ledStart, int ledEnd, Properties settings) {
        this.strip = strip;
        this.leds = new int[1][];
        int[] leds = new int[ledEnd-ledStart];
        int index = 0;
        for(int i = ledStart; i < ledEnd; i++)
            leds[index++] = i;
        this.leds[0] = leds;
        this.settings = settings;
    }

    protected final Ws281xLedStrip strip;

    protected final int[][] leds;

    protected final Properties settings;

    public abstract void start();

    public abstract void loop();

}
