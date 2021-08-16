package com.cryo.web.modules;

import com.cryo.entities.annotations.EndpointSubscriber;
import com.cryo.entities.annotations.SPAEndpoint;
import spark.Request;
import spark.Response;

import java.util.HashMap;

import static com.cryo.utils.Utilities.renderPage;

@EndpointSubscriber
public class IndexModule {

	@SPAEndpoint("/")
	public static String renderIndexPage(Request request, Response response) {
		return renderPage("index", new HashMap<>(), request, response);
	}
}
