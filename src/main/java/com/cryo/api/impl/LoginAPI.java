package com.cryo.api.impl;

import com.cryo.RoomController;
import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.Token;
import com.cryo.entities.annotations.Endpoint;
import com.cryo.entities.annotations.EndpointSubscriber;
import com.cryo.entities.annotations.SPAEndpoint;
import com.cryo.utils.BCrypt;
import com.cryo.utils.SessionIDGenerator;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.sql.Timestamp;
import java.util.Properties;

import static com.cryo.RoomController.getConnection;
import static com.cryo.api.APIController.error;
import static com.cryo.api.APIController.success;

@Slf4j
@EndpointSubscriber
public class LoginAPI {

	@Endpoint(endpoint="POST", method="/review")
	public static String reviewKey(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Invalid token");
		return success("");
	}

	@SPAEndpoint("/logout")
	public static String logout(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("Invalid token");
		String token = request.queryParams("token");
		boolean revoke = false;
		if(request.queryParams().contains("revoke")) {
			try {
				revoke = Boolean.parseBoolean(request.queryParams("revoke"));
			} catch(Exception e) {
				e.printStackTrace();
				return error("Error parsing revoke. Please try again.");
			}
		}
		if(revoke)
			getConnection().delete("tokens", "account_id=?", account.getId());
		else
			getConnection().delete("tokens", "token=?", token);
		return success("");
	}
}
