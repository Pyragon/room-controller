package com.cryo.api.impl;

import com.cryo.RoomController;
import com.cryo.api.AccountUtils;
import com.cryo.controllers.StripController;
import com.cryo.entities.Account;
import com.cryo.entities.EffectData;
import com.cryo.entities.Scene;
import com.cryo.entities.Strip;
import com.cryo.entities.annotations.Endpoint;
import com.cryo.entities.annotations.EndpointSubscriber;
import spark.Request;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Properties;

import static com.cryo.ConnectionManager.getGson;
import static com.cryo.RoomController.getConnection;
import static com.cryo.api.APIController.error;
import static com.cryo.api.APIController.success;

@EndpointSubscriber
public class SceneAPI {

	@Endpoint(method = "POST", endpoint="/api/scenes/all")
	public static String getScenes(Request request) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Insufficient permissions. Please login and try again.");
		ArrayList<Scene> scenes = getConnection().selectList("scenes", Scene.class);
		Properties prop = new Properties();
		prop.put("success", true);
		prop.put("scenes", getGson().toJson(scenes.toArray(Scene[]::new)));
		return getGson().toJson(prop);
	}

	@Endpoint(method = "POST", endpoint="/api/scenes/:id")
	public static String getScene(Request request) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Insufficient permissions. Please login and try again.");
		String idString = request.params(":id");
		if(idString == null) return error("Error parsing id string. Please try again.");
		int id;
		try {
			id = Integer.parseInt(idString);
		} catch(Exception e) {
			return error("Error parsing id string. Please try again.");
		}
		Scene scene = getConnection().selectClass("scenes", "id=?", Scene.class, id);
		if(scene == null) return error("Cannot find scene. Please try again.");
		Properties prop = new Properties();
		prop.put("success", true);
		prop.put("scene", scene);
		return getGson().toJson(prop);
	}

	@Endpoint(method = "POST", endpoint = "/api/strip/:stripId/addscene/:sceneId")
	public static String setSceneActive(Request request) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Insufficient permissions. Please login and try again.");
		String stripIdString = request.params(":stripId");
		String sceneIdString = request.params(":sceneId");
		if(stripIdString == null) return error("Error parsing strip id string. Please try again.");
		if(sceneIdString == null) return error("Error parsing scene id string. Please try again.");
		int stripId;
		int sceneId;
		try {
			stripId = Integer.parseInt(stripIdString);
		} catch(Exception e) {
			return error("Error parsing strip id string. Please try again.");
		}
		try {
			sceneId = Integer.parseInt(sceneIdString);
		} catch(Exception e) {
			return error("Error parsing scene id string. Please try again.");
		}
		Strip strip = RoomController.getINSTANCE().getStripController().getStrips().get(stripId);
		if(strip == null) return error("Unable to find strip. Please try again.");
		Scene scene = getConnection().selectClass("scenes", "id=?", Scene.class, sceneId);
		if(scene == null) return error("Unable to find scene. Please try again.");
		strip.getActiveScenes().put(scene.getId(), scene);
		String json = getGson().toJson(strip.getActiveScenes().keySet().toArray(Integer[]::new));
		getConnection().set("strips", "scenes=?", "id=?", json, scene.getId());
		return success("");
	}

	@Endpoint(method = "POST", endpoint = "/api/scenes/create")
	public static String createScene(Request request) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Insufficient permissions. Please login and try again.");
		String name = request.queryParams("name");
		if(name == null || name.equals("") || name.length() < 3 || name.length() > 50)
			return error("Invalid name. Please provide a name between 3 and 50 characters.");
		Time timeToStart = null;
		boolean active;
		if(request.queryParams().contains("time_to_start")) {
			try {
				String millisString = request.queryParams("time_to_start");
				long millis = Long.parseLong(millisString);
				timeToStart = new Time(millis);
			} catch(Exception e) {
				e.printStackTrace();
				return error("Error parsing time to start. Please try again.");
			}
		}
		try {
			String activeString = request.queryParams("active");
			active = Boolean.parseBoolean(activeString);
		} catch(Exception e) {
			e.printStackTrace();
			return error("Error parsing active string. Please try again.");
		}
		String effectDataString = request.queryParams("effect_data");
		EffectData[] data;
		try {
			data = getGson().fromJson(effectDataString, EffectData[].class);
			if(data == null) return error("Error parsing effect data. Please try again.");
		} catch(Exception e) {
			e.printStackTrace();
			return error("Error parsing effect data. Please try again.");
		}
		Scene scene = new Scene(-1, name, timeToStart, getGson().toJson(data), null, null);
		getConnection().insert("scenes", scene.data());
		return success("");
	}
}
