package com.cryo.web;

import com.cryo.RoomController;
import com.cryo.utils.CorsFilter;
import com.cryo.utils.Utilities;
import spark.ExceptionHandler;
import spark.Request;
import spark.Response;

import static spark.Spark.*;
import static spark.Spark.staticFiles;

public class WebController {

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

}
