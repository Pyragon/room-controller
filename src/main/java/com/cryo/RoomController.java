package com.cryo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.pi3g.pi.ws2812.WS2812;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Data
@Slf4j
public class RoomController {

	@Getter
	public static RoomController INSTANCE;
	@Getter
	private static Properties properties;
	@Getter
	private static Gson gson;
	@Getter
	private static ConnectionManager connection;

//	private Ws281xLedStrip strip;

	public void start() {
		buildGson();
		loadProperties();

		connection = new ConnectionManager();

		int ledsCount = Integer.parseInt(properties.getProperty("leds_count"));
		WS2812.get().init(64); //init a chain of 64 LEDs
		WS2812.get().clear();
//		strip = new Ws281xLedStrip(ledsCount, 10, 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_BGR, false);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			private boolean on;

			@Override
			public void run() {

				if(on) {
					WS2812.get().setPixelColor(0, Color.RED); //sets the color of the fist LED to red
//					strip.setStrip(new Color(0, 0, 0));
					log.info("Set strip off");
				} else {
					WS2812.get().setPixelColor(0, Color.BLACK); //sets the color of the fist LED to red
//					strip.setStrip(new Color(255, 0, 0));
					log.info("Set strip on");
				}
				WS2812.get().show();
//				strip.render();
				on = !on;
			}
		}, 1000, 1000);

	}

	public static void loadProperties() {
		File file = new File("data/props.json");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder builder = new StringBuilder();
			while((line = reader.readLine()) != null)
				builder.append(line);
			String json = builder.toString();
			properties = gson.fromJson(json, Properties.class);
			reader.close();
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties!", e);
		}
	}

	public static void buildGson() {
		gson = new GsonBuilder()
				.serializeNulls()
				.setVersion(1.0)
				.disableHtmlEscaping()
				.setPrettyPrinting()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.create();
	}

	public static void main(String[] args) {
		INSTANCE = new RoomController();
		INSTANCE.start();
	}
}
