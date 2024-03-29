package com.cryo;

import com.cryo.controllers.StripController;
import com.cryo.controllers.SceneController;
import com.cryo.tasks.TaskManager;
import com.cryo.utils.Utilities;
import com.cryo.api.APIController;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Timer;

@Data
@Slf4j
public class RoomController {

	@Getter
	public static RoomController INSTANCE;
	@Getter
	private static Properties properties;
	@Getter
	private static Gson gson;
	private static ConnectionManager connection;

	private SceneController sceneController;
	private StripController stripController;
	private APIController apiController;

	private Timer executor;

	public void start() {
		buildGson();
		loadProperties();

		connection = new ConnectionManager();

		Utilities.sendStartupHooks();

		stripController = new StripController();
		sceneController = new SceneController(stripController);
		apiController = new APIController();
		executor = new Timer();

		sceneController.load();
		stripController.load();
		apiController.load();

		executor.scheduleAtFixedRate(new TaskManager(), 2000, 10);

	}

	public static DBConnection getConnection() {
		return connection.getConnection("room_controller");
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
