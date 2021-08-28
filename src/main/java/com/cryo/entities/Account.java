package com.cryo.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
public class Account extends MySQLDao {

	@MySQLDefault
	private final int id;
	private final String username;
	private final String displayName;
	private final String salt;
	private final String hash;
	@MySQLDefault
	private final Timestamp added;
	@MySQLDefault
	private final Timestamp updated;
}
