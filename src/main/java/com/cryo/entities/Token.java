package com.cryo.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@Data
@EqualsAndHashCode(callSuper = false)
public class Token extends MySQLDao {

	private final int id;
	private final int accountId;
	private final String token;
	private final Timestamp expiry;
	private final Timestamp added;

}
