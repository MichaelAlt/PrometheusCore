package com.prometheus.core.base.identities.attributes;

/**
 * This class represents an identity attribute. It is used to manage one
 * specific attribute.
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
	 * Default constructor of the class.
	 * 
	 * @param attributeManagement
	 *            The management object for the attributes
	 */
	protected IdentityAttribute(IdentityAttributeManagement attributeManagement) {
		this.attributeManagement = attributeManagement;
	}

	/**
	 * Get the unique key of the attribute.
	 * 
	 * @return The key as string
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Get the unique name of the attribute.
	 * 
	 * @return The name as string
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the datatype of the attribute.
	 * 
	 * @return The datatype as integer
	 */
	public int getType() {
		return type;
	}

	/**
	 * Get the maximum length of the attribute.
	 * 
	 * @return The maximum length as integer
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Does the attribute allow null values or not.
	 * 
	 * @return Allowed true, not allowed false
	 */
	public boolean isNullable() {
		return nullable;
	}

	/**
	 * Get the default value of the attribute.
	 * 
	 * @return The default value as string
	 */
	public String getDefault() {
		return defaultValue;
	}

	/**
	 * Change the name of the attribute.
	 * 
	 * @param name
	 *            The new name of the attribute
	 */
	public void changeName(String name) {
		this.attributeManagement.modify(this, name, type, length, nullable, defaultValue);
	}

	/**
	 * Change the settings of the attribute field.
	 * 
	 * @param type
	 *            The type of the field (VARCHAR,INT etc.)
	 * @param length
	 *            The length of the field
	 * @param nullable
	 *            If the field allows null values
	 * @param defaultValue
	 *            The default value of the field
	 */
	public void modify(int type, int length, boolean nullable, String defaultValue) {
		this.attributeManagement.modify(this, name, type, length, nullable, defaultValue);
	}

	/**
	 * Delete the attribute.
	 * 
	 */
	public void delete() {
		this.attributeManagement.delete(this);
	}

}