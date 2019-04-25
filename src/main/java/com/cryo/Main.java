package com.cryo;

import com.cryo.controllers.effects.EffectsController;
import com.cryo.controllers.LEDController;
import com.cryo.db.DBConnectionManager;
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
    private EffectsController effectsController;
    private DBConnectionManager connectionManager;

    private Properties properties;

    @Getter
    private static Main instance;

    @Getter
    private static Gson gson;

    public static void main(String[] args) {

        Ws281xLedStrip strip = new Ws281xLedStrip(200, 10, 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_GRB, false);
        strip.setStrip(255, 0, 0);
        strip.render();

//        WS281xTest.main(args);
//        instance = new Main();
//        instance.setup();
    }

    public void setup() {
        gson = buildGson();
        loadProperties();
        connectionManager = new DBConnectionManager();
        ledController = new LEDController(1); //CHANGE
        effectsController = new EffectsController();

        ledController.setupEndpoints();
        effectsController.setupEndpoints();

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
