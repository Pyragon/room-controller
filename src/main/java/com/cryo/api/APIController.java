package com.cryo.api;

import com.cryo.RoomController;
import com.cryo.utils.CorsFilter;
import com.cryo.utils.Utilities;
import spark.ExceptionHandler;
import spark.Response;

import java.util.Properties;

import static spark.Spark.*;
import static spark.Spark.staticFiles;

public class APIController {

	public void load() {

		port(Integer.parseInt(RoomController.getProperties().getProperty("port")));
		exception(Exception.class, handleExceptions());
		staticFiles.externalLocation("web/public/");
		staticFiles.expireTime(0); // ten minutes
		staticFiles.header("Access-Control-Allow-Origin", "*");
		CorsFilter.apply();

		Utilities.initializeEndpoints();
	}

	public ExceptionHandler<Exception> handleExceptions() {
		return (e, req, res) -> render500(res, e.getMessage());
	}

	public static void render500(Response response, String message) {
		response.status(500);
		response.body("<h1>Exception occurred</h1><div>" + message + "</div>");
	}

	public static String success(String message) {
		Properties prop = new Properties();
		prop.put("success", true);
		prop.put("message", message);
		return RoomController.getGson().toJson(prop);
	}

	public static String error(String message) {
		Properties prop = new Properties();
		prop.put("success", false);
		prop.put("error", message);
		return RoomController.getGson().toJson(prop);
	}

}
