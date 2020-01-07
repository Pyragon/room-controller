package com.cryo.controllers;

import com.cryo.Main;
import com.cryo.controllers.effects.impl.BlinkEffect;
import com.cryo.controllers.effects.impl.ContinousEffect;
import com.cryo.db.impl.MiscConnection;
import com.cryo.db.impl.SceneConnection;
import com.cryo.entities.Effect;
import com.cryo.entities.Scene;
import com.github.mbelling.ws281x.Color;
import com.github.mbelling.ws281x.Ws281xLedStrip;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

public class SceneController {

    private HashMap<String, Scene> scenes;

    private Scene scene;
    private Scene quickScene;

    public SceneController() {
        scenes = new HashMap<>();
        Object[] data = SceneConnection.connection().handleRequest("get-scenes");
        if(data != null) {
            ArrayList<Scene> scenes = (ArrayList<Scene>) data[0];
            scenes.forEach(s -> this.scenes.put(s.getName(), s));
        }
        String lastScene = MiscConnection.getString("last-scene");
        if(lastScene == null) lastScene = "default";
        if(scenes.containsKey(lastScene)) {
            setScene(scenes.get(lastScene));
            return;
        }
        Properties settings = new Properties();
        settings.put("colour", new Color(255, 0, 0));
        setScene(new Scene(-1, "ContinuousTest", new Effect[]{new BlinkEffect(getStrip(), 0, 20, settings) }, new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime())));
    }

    public void setQuickScene(Scene scene) {
        getStrip().setStrip(0, 0, 0);
        getStrip().render();
        this.quickScene = scene;
        this.quickScene.start();
        this.quickScene.setOnEnd(() -> this.quickScene = null);
    }

    public void setScene(Scene scene) {
        getStrip().setStrip(0, 0, 0);
        getStrip().render();
        this.scene = scene;
        this.scene.start();
        this.scene.setOnEnd(() -> this.scene = null);
    }

    public void stopScene() {
        if(quickScene != null) quickScene.end();
        if(scene != null) scene.end();
    }

    public void loop() {
        if(quickScene != null) {
            quickScene.loop();
            return;
        }
        if(scene == null || !scene.isStarted()) return;
        scene.loop();
    }

    public Ws281xLedStrip getStrip() {
        return Main.getInstance().getLedController().getStrip();
    }
}
