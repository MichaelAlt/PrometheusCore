package com.prometheus.core.base.repositories.settings;

/**
 * 
 * @author alt
 *
 */
public class RepositorySetting {

	protected String setting_key;
	protected String setting_repository;
	protected String setting_name;
	protected String setting_value;

	protected RepositorySetting() {

	}

	public String getSetting_key() {
		return setting_key;
	}

	public String getSetting_repository() {
		return setting_repository;
	}

	public String getSetting_name() {
		return setting_name;
	}

	public String getSetting_value() {
		return setting_value;
	}

}