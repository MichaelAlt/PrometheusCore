package com.prometheus.core.base.identities.attributes;

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
	protected boolean nullable;
	protected String defaultValue;

	protected IdentityAttributeManagement attributeManagement;

	/**
	 * 
	 * @param attributeManagement
	 */
	protected IdentityAttribute(IdentityAttributeManagement attributeManagement) {
		this.attributeManagement = attributeManagement;
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

	public boolean isNullable() {
		return nullable;
	}

	public String getDefault() {
		return defaultValue;
	}

	public void changeName(String name) {
		this.attributeManagement.modifyAttribute(this, name, type, length, nullable, defaultValue);
	}

	public void modify(int type, int length, boolean nullable, String defaultValue) {
		this.attributeManagement.modifyAttribute(this, name, type, length, nullable, defaultValue);
	}

	public void delete() {
		this.attributeManagement.deleteAttribute(this);
	}

}