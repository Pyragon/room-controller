package com.cryo.api.impl;

import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.Strip;
import com.cryo.entities.annotations.Endpoint;
import com.cryo.entities.annotations.EndpointSubscriber;
import lombok.Data;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Properties;

import static com.cryo.ConnectionManager.getGson;
import static com.cryo.RoomController.getConnection;
import static com.cryo.api.APIController.error;

@EndpointSubscriber
@Data
public class StripAPI {

	@Endpoint(method = "POST", endpoint = "/api/strips")
	public static String getStrips(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Invalid token");
		List<Strip> strips = getConnection().selectList("strips", Strip.class);
		Properties prop = new Properties();
		prop.put("success", true);
		prop.put("strips", strips);
		return getGson().toJson(prop);
	}
}
