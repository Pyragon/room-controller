package com.cryo.entities;

import lombok.Data;

@Data
public abstract class Effect {

    protected final int startIndex;
    protected final int endIndex;

    public abstract void loop();

    public void showStrip() {
        //SEND LEDs to LEDController to be shown
    }

}
