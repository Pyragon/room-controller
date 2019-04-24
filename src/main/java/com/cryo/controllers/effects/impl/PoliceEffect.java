package com.cryo.controllers.effects.impl;

import com.cryo.entities.Effect;

public class PoliceEffect extends Effect {

    private int stage;

    public PoliceEffect(int startIndex, int endIndex) {
        super(startIndex, endIndex);
    }

    @Override
    public void loop() {
        int length = endIndex-startIndex;
        int middle = length/5;
        int mLength = (length-middle)/2;
        //length = 20
        //middle = 4
        //mLength = (20-4)/2
        //mLength = 8
        for(int i = startIndex; i < startIndex+mLength; i++) {

        }
        for(int i = startIndex+mLength; i < startIndex+mLength+middle; i++) {

        }
        for(int i = startIndex+mLength+middle; i < endIndex+1; i++) {

        }
    }
}
