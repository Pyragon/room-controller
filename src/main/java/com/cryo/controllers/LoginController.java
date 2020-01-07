package com.cryo.controllers;

import com.cryo.Main;
import com.cryo.db.impl.LoginConnection;
import com.cryo.entities.Controller;
import com.cryo.utils.SessionIDGenerator;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Properties;

public class LoginController implements Controller {
    @Override
    public String[] getRoutes() {
        return new String[] { "POST", "/login" };
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        Properties prop = new Properties();
        HashMap<String, Object> model = new HashMap<>();
        switch(endpoint) {
        }
        return Main.getGson().toJson(prop);
    }
}
