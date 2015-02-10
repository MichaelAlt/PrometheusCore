package com.prometheus.core.identity.base;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 
 * @author alt
 *
 */
public class Identity {

	protected String key;
	protected Timestamp created;
	protected Timestamp changed;

	protected HashMap<String, Object> attributes;

	/**
	 * 
	 */
	public Identity() {
		this.attributes = new HashMap<String, Object>();
	}

	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * 
	 * @return
	 */
	public Timestamp getCreated() {
		return this.created;
	}

	/**
	 * 
	 * @return
	 */
	public Timestamp getChange() {
		return this.changed;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

}