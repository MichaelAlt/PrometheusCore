package com.prometheus.core.configuration;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 
 * @author alt
 *
 */
public class Configuration {

	private HashMap<String, String> properties;

	/**
	 * 
	 */
	public Configuration() {
		this.properties = new HashMap<String, String>();
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected void put(String key, String value) {
		this.properties.put(key, value);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return this.properties.get(key);
	}

	/**
	 * 
	 * @param index
	 * @return
	 */
	public String get(int index) {

		int current = 0;

		for (Entry<String, String> entry : this.properties.entrySet()) {

			if (current == index) {
				return entry.getValue();
			}

			current++;
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		return this.properties.size();
	}

}