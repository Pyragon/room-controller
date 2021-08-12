package com.cryo;

import com.github.mbelling.ws281x.Color;
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

@Data
public class RoomController {

	@Getter
	public static RoomController INSTANCE;
	@Getter
	private static Properties properties;
	@Getter
	private static Gson gson;
	@Getter
	private static ConnectionManager connection;

	private Ws281xLedStrip strip;

	public void start() {
		buildGson();
		loadProperties();

		connection = new ConnectionManager();

		int ledsCount = Integer.parseInt(properties.getProperty("leds_count"));
		strip = new Ws281xLedStrip(ledsCount, 10, 800000, 10, 255, 0, false, LedStripType.WS2811_STRIP_BGR, false);

		strip.setStrip(Color.RED);
		strip.render();
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
