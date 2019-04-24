package com.cryo.controllers.effects;

import com.cryo.entities.Controller;
import spark.Request;
import spark.Response;

public class EffectsController implements Controller {

    @Override
    public String[] getRoutes() {
        return new String[0];
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        return "";
    }
}
