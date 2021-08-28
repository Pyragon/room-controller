package com.cryo.api;

import com.cryo.entities.Account;
import com.cryo.entities.Token;
import spark.Request;

import static com.cryo.RoomController.getConnection;

public class AccountUtils {

	public static Account getAccount(Request request) {
		if(request.queryParams().contains("token")) return null;
		String tokenString = request.queryParams("token");
		if(tokenString == null || tokenString.equals("")) return null;
		Token token = getConnection().selectClass("tokens", "token=?", Token.class, tokenString);
		if(token == null) return null;
		if(token.getExpiry().getTime() < System.currentTimeMillis()) return null;
		return getConnection().selectClass("accounts", "account_id=?", Account.class, token.getAccountId());
	}
}
