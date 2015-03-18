package com.prometheus.core.base.identities;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * 
 * @author alt
 *
 */
public class Identity {

	protected String key;
	protected String unique;
	protected Timestamp created;
	protected Timestamp changed;

	protected HashMap<String, String> attributes;
	protected IdentityManagement identityManagement;

	/**
	 * 
	 */
	protected Identity(IdentityManagement identityManagement) {

		this.attributes = new HashMap<String, String>();
		this.identityManagement = identityManagement;
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
	public String getUnique() {
		return this.unique;
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
	public String getAttribute(String name) {
		return this.attributes.get(name.toLowerCase());
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void changeAttribute(String name, String value) {

		this.identityManagement.changeAttribute(this, name, value);
	}

	/**
	 * 
	 * @param attributes
	 */
	public void ChangeAttributes(HashMap<String, String> attributes) {

		this.identityManagement.changeAttributes(this, attributes);
	}

}