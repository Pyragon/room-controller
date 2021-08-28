package com.cryo.api.impl;

import com.cryo.RoomController;
import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.Token;
import com.cryo.entities.annotations.EndpointSubscriber;
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

	public static String login(Request request, Response response) {
		if(!request.queryParams().contains("username") || !request.queryParams().contains("password"))
			return error("Error parsing login info. Please try again.");
		String username = request.queryParams("username");
		String password = request.queryParams("password");
		Account account = getConnection().selectClass("accounts", "username LIKE ?", Account.class, username);
		if(account == null) return error("Invalid username or password. Please try again.");
		if(!BCrypt.hashPassword(password, account.getSalt()).equals(account.getHash())) return error("Invalid username or password. Please try again.");
		String tokenId = SessionIDGenerator.getInstance().getSessionId();
		Timestamp expiry = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
		Token token = new Token(-1, account.getId(), tokenId, expiry, null);
		getConnection().insert("tokens", token);
		Properties prop = new Properties();
		prop.put("success", true);
		prop.put("token", token.getToken());
		return RoomController.getGson().toJson(prop);
	}

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
