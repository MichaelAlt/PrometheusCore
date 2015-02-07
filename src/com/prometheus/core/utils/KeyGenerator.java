package com.prometheus.core.utils;

import java.util.UUID;

/**
 * 
 * @author alt
 *
 */
public class KeyGenerator {

	/**
	 * 
	 * @return
	 */
	public static String generateKey() {
		return UUID.randomUUID().toString().toUpperCase();
	}

}