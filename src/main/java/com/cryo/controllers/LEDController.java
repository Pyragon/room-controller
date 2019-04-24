package com.cryo.controllers;

import com.cryo.entities.Controller;
import spark.Request;
import spark.Response;

public class LEDController implements Controller {

    private byte[] leds;

    public LEDController(int ledsCount) {
        leds = new byte[ledsCount];

    }

    @Override
    public String[] getRoutes() {
        return new String[] { "GET", "/leds", "POST", "/leds/:led/:action", "POST", "/leds/reset" };
                                //prints info of all LEDs   sets led on/of              resets all LEDs to normal
    }

    @Override
    public String decodeRequest(String endpoint, Request request, Response response) {
        //CHECK FOR LOGIN

        return "";
    }
}
