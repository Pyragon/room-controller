package com.cryo.api.impl;

import com.cryo.api.AccountUtils;
import com.cryo.entities.Account;
import com.cryo.entities.Session;
import com.cryo.entities.annotations.EndpointSubscriber;
import com.cryo.utils.BCrypt;
import com.cryo.utils.SessionIDGenerator;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.sql.Timestamp;

import static com.cryo.RoomController.getConnection;
import static com.cryo.api.APIController.error;
import static com.cryo.api.APIController.success;

@Slf4j
@EndpointSubscriber
public class LoginAPI {

	public static String login(Request request, Response response) {
		if(request.cookies().containsKey("room_session"))
			response.removeCookie("room_session");
		if(!request.queryParams().contains("username") || !request.queryParams().contains("password"))
			return error("Error parsing login info. Please try again.");
		String username = request.queryParams("username");
		String password = request.queryParams("password");
		Account account = getConnection().selectClass("accounts", "username LIKE ?", Account.class, username);
		if(account == null) return error("Invalid username or password. Please try again.");
		if(!BCrypt.hashPassword(password, account.getSalt()).equals(account.getHash())) return error("Invalid username or password. Please try again.");
		String sessionId = SessionIDGenerator.getInstance().getSessionId();
		response.cookie("room_session", sessionId, (60 * 60 * 24));
		Timestamp expiry = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
		Session session = new Session(-1, account.getId(), sessionId, expiry, null);
		getConnection().insert("sessions", session);
		return success("");
	}

	public static String logout(Request request, Response response) {
		Account account = AccountUtils.getAccount(request);
		if(account == null) return error("You are not logged in.");
		boolean revoke = false;
		if(request.queryParams().contains("revoke")) {
			try {
				revoke = Boolean.parseBoolean(request.queryParams("revoke"));
			} catch(Exception e) {
				e.printStackTrace();
				return error("Error parsing revoke. Please try again.");
			}
		}
		String sessionId = request.cookie("room_session");
		if(revoke)
			getConnection().delete("sessions", "account_id=?", account.getId());
		else
			getConnection().delete("sessions", "session_id=?", sessionId);
		return success("");
	}
}
