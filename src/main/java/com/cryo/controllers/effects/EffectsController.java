package com.cryo.controllers.effects;

import com.cryo.entities.Controller;
import com.cryo.entities.Effect;
import spark.Request;
import spark.Response;

import java.util.HashMap;

public class EffectsController implements Controller {

    private HashMap<String, Class<Effect>> effects;

    public EffectsController() {
        effects = new HashMap<>();
    }

    @Override
    public String[] getRoutes() {
        return new String[0];
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        return "";
    }
}
