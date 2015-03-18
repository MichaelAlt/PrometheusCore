package com.prometheus.core.base.repositories;

/**
 * Represents a setting for an repository.
 * 
 * @author alt
 *
 */
public class RepositorySetting {

	protected String key;
	protected Repository repository;
	protected String name;
	protected String value;

	private RepositoryManagement repositoryManagement;

	/**
	 * The default constructor of the class.
	 * 
	 * @param repositoryManagement
	 *            The management object of the class
	 */
	protected RepositorySetting(RepositoryManagement repositoryManagement) {
		this.repositoryManagement = repositoryManagement;
	}

	/**
	 * Get the unique key of the repository setting.
	 * 
	 * @return The key as a string
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Get the repository of the setting.
	 * 
	 * @return The parent repository of the setting
	 */
	public Repository getRepository() {
		return this.repository;
	}

	/**
	 * Get the unique name of the setting.
	 * 
	 * @return The name of the setting
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the value of the setting.
	 * 
	 * @return The value of the setting
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * Change the name of the setting.
	 * 
	 * @param name
	 *            The new name of the setting
	 */
	public void rename(String name) {
		this.repositoryManagement.modifySetting(this, name, this.value);
	}

	/**
	 * Change the value of the setting.
	 * 
	 * @param value
	 *            The new value of the setting
	 */
	public void modify(String value) {
		this.repositoryManagement.modifySetting(this, this.name, value);
	}

	/**
	 * Change the name and the value of the setting.
	 * 
	 * @param name
	 *            The new name of the setting
	 * @param value
	 *            The new value of the setting
	 */
	public void modify(String name, String value) {
		this.repositoryManagement.modifySetting(this, name, value);
	}

	/**
	 * Delete the setting.
	 * 
	 */
	public void delete() {
		this.repositoryManagement.deleteSetting(this);
	}

}