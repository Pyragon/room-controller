package com.cryo.entities;

import com.cryo.effects.Effect;
import lombok.Data;

import java.util.Properties;

@Data
public class EffectData {

	private final int[] leds;
	private final String effectName;

	private final Properties settings;


}
