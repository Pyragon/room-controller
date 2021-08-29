package com.cryo.api.impl;

import com.cryo.RoomController;
import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.annotations.Endpoint;
import com.cryo.entities.annotations.EndpointSubscriber;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Properties;
import java.util.Random;

import static com.cryo.api.APIController.error;

@Slf4j
@EndpointSubscriber
public class UserAPI {

	private static final Random RANDOM = new Random();

	private static final String[] MESSAGES = { "Welcome back, %name%.", "How're ya now?", "Good, n'you, %name%?", "Get lit, %name%" };

	@Endpoint(method="POST", endpoint="/user")
	public static String getUserInfo(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Invalid token");
		Properties prop = new Properties();
		prop.put("displayName", account.getDisplayName());
		prop.put("imageName", account.getImageName());
		prop.put("message", MESSAGES[RANDOM.nextInt(MESSAGES.length)]);
		prop.put("success", true);
		return RoomController.getGson().toJson(prop);
	}
}
