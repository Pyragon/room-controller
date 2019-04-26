package com.cryo;

import com.cryo.controllers.SceneController;
import com.cryo.controllers.effects.EffectsController;
import com.cryo.controllers.LEDController;
import com.cryo.db.DBConnectionManager;
import com.cryo.db.impl.SceneConnection;
import com.cryo.entities.Scene;
import com.cryo.tasks.TaskManager;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static spark.Spark.*;

@Data
public class Main {

    private LEDController ledController;
    private SceneController sceneController;
    private EffectsController effectsController;
    private DBConnectionManager connectionManager;
    private TaskManager taskManager;

    private Properties properties;

    @Getter
    private static Main instance;

    @Getter
    private static Gson gson;

    public static void main(String[] args) {
        instance = new Main();
        instance.setup();
    }

    public void setup() {
        gson = buildGson();
        loadProperties();
        connectionManager = new DBConnectionManager();
        connectionManager.init();
        ledController = new LEDController(); //CHANGE
        sceneController = new SceneController();
        effectsController = new EffectsController();
        taskManager = new TaskManager();

        ledController.setupEndpoints();
        effectsController.setupEndpoints();
        taskManager.init();

        get("/", (req, res) -> {
            return "";
        });
    }

    public static Gson buildGson() {
        return new GsonBuilder().serializeNulls().setVersion(1.0).disableHtmlEscaping().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
    }

    public void loadProperties() {
        File file = new File("props.json");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder json = new StringBuilder();
            while ((line = reader.readLine()) != null) json.append(line);
            properties = getGson().fromJson(json.toString(), Properties.class);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
