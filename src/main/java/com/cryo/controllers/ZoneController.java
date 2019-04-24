package com.cryo.controllers;

import com.cryo.entities.Controller;
import spark.Request;
import spark.Response;

public class ZoneController implements Controller {

    @Override
    public String[] getRoutes() {
        return new String[0];
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        return "";
    }
}
