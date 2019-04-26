package com.cryo.controllers;

import com.cryo.Main;
import com.cryo.entities.Controller;
import com.github.mbelling.ws281x.LedStripType;
import com.github.mbelling.ws281x.Ws281xLedStrip;
import lombok.Data;
import spark.Request;
import spark.Response;

@Data
public class LEDController implements Controller {

    private Ws281xLedStrip strip;

    public LEDController() {
        start();
    }

    public void start() {
        int ledsCount = Integer.parseInt(Main.getInstance().getProperties().getProperty("leds_count"));
        strip = new Ws281xLedStrip(ledsCount, 10, 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_GRB, false);
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
