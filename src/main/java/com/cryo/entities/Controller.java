package com.cryo.entities;

import spark.Request;
import spark.Response;

import static spark.Spark.get;
import static spark.Spark.post;

public interface Controller {

    String[] getRoutes();

    String decodeRequest(String endpoint, Request request, Response response);

    default void setupEndpoints() {
        int index = 0;
        while(index < getRoutes().length) {
            String method = getRoutes()[index++];
            String route = getRoutes()[index++];
            if (method.equals("GET")) get(route, (req, res) -> decodeRequest(route, req, res));
            else post(route, (req, res) -> decodeRequest(route, req, res));
        }
    }

}
