package com.cryo.entities;

import com.cryo.RoomController;
import lombok.Data;

/**
 * @author Cody Thompson <eldo.imo.rs@hotmail.com>
 *
 * Created on: March 13, 2017 at 2:59:27 PM
 */
@Data
public abstract class Task {

	private long nextRun;

	private final long delay;

	public abstract void run();

}
