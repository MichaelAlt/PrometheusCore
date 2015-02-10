package com.prometheus.core.identity.base.attributes;

/**
 * 
 * @author alt
 *
 */
public class IdentityAttribute {

	protected String key;
	protected String name;
	protected int type;
	protected int length;
	protected boolean index;

	/**
	 * 
	 */
	public IdentityAttribute() {

	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public int getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public boolean isIndex() {
		return index;
	}

}