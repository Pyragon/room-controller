package com.cryo.api;

import com.cryo.entities.Account;
import com.cryo.entities.Session;
import spark.Request;

import static com.cryo.RoomController.getConnection;

public class AccountUtils {

	public static Account getAccount(Request request) {
		if(!request.cookies().containsKey("room_session")) return null;
		String sessionId = request.cookie("room_session");
		if(sessionId == null || sessionId.equals("")) return null;
		Session session = getConnection().selectClass("sessions", "session_id=?", Session.class, sessionId);
		if(session == null) return null;
		if(session.getExpiry().getTime() < System.currentTimeMillis()) return null;
		return getConnection().selectClass("accounts", "account_id=?", Account.class, session.getAccountId());
	}
}
