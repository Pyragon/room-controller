package com.cryo.entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Scene {

    private final int id;
    private final String name;
    private final Effect[] effects;
    private final Timestamp added;
    private final Timestamp updated;

    private boolean started;
    private Runnable onEnd;

    public void start() {
        for(Effect effect : this.effects) effect.start();
        started = true;
    }

    public void loop() {
        for(Effect effect : effects) effect.loop();
    }

    public void end() {
        if(onEnd != null) onEnd.run();
    }
}
