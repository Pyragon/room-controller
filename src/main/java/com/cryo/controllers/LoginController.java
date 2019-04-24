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

    public static boolean isLoggedIn(Request request) {
        if(!request.cookies().containsKey("led_controller_session")) return false;
        String session_id = request.cookie("led_controller_session");
        Object[] data = LoginConnection.connection().handleRequest("get-session", session_id);
        return data != null;
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        Properties prop = new Properties();
        HashMap<String, Object> model = new HashMap<>();
        switch(endpoint) {
            case "/login":
                if(isLoggedIn(request)) {
                    prop.put("success", false);
                    prop.put("error", "Already logged in.");
                    break;
                }
                String username = request.queryParams("username");
                String password = request.queryParams("password");
                Object[] data = LoginConnection.connection().handleRequest("compare", username, password);
                if(data == null) {
                    prop.put("success", false);
                    prop.put("error", "Invalid username or password.");
                    break;
                }
                String session_id = SessionIDGenerator.getInstance().getSessionId();
                response.cookie("led_controller_session", session_id);
                long time = System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 30);
                LoginConnection.connection().handleRequest("set-session", username, session_id, time);
                prop.put("success", true);
                break;
            case "/logout":
                if(!isLoggedIn(request)) {
                    prop.put("success", false);
                    prop.put("error", "Not logged in.");
                    break;
                }
                session_id = request.cookie("led_controller_session");
                response.removeCookie("led_controller_session");
                LoginConnection.connection().handleRequest("remove-session", session_id);
                prop.put("success", true);
                break;
        }
        return Main.getGson().toJson(prop);
    }
}
