package com.cryo.entities;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Account extends MySQLDao {

	private final int id;
	private final String username;
	private final String displayName;
	private final String salt;
	private final String hash;
	private final Timestamp added;
	private final Timestamp updated;
}
