package com.prometheus.core.base.repositories;

import java.util.List;

import com.prometheus.core.base.identities.attributes.IdentityAttribute;

/**
 * This class represents a repository. A repository is the sum of configuration
 * settings for a specific system that is connected to the Prometheus Identity
 * Management.
 * 
 * @author alt
 *
 */
public class Repository {

	protected String key;
	protected String name;
	protected IdentityAttribute attribute;

	protected RepositoryManagement repositoryManagement;

	/**
	 * The default construtor of the class.
	 * 
	 * @param repositoryManagement
	 *            The management object of the class
	 */
	protected Repository(RepositoryManagement repositoryManagement) {
		this.repositoryManagement = repositoryManagement;
	}

	/**
	 * Get the unique key of the repository.
	 * 
	 * @return The key as a string
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Get the unique name of the repository.
	 * 
	 * @return The name as a string
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the membership identity attribute for the repository.
	 * 
	 * @return The membership attribute as IdentityAttribute
	 */
	public IdentityAttribute getAttribute() {
		return this.attribute;
	}

	/**
	 * Change the name of the repository.
	 * 
	 * @param name
	 *            The new name of the repository
	 */
	public void rename(String name) {
		this.repositoryManagement.modifyRepository(this, name);
	}

	/**
	 * Delete the repository.
	 * 
	 */
	public void delete() {
		this.repositoryManagement.deleteRepository(this);
	}

	/**
	 * Get a setting of the repository with it's unique key or name.
	 * 
	 * @param query
	 *            The key or name of the setting
	 * @return The setting as RepositorySetting
	 */
	public RepositorySetting getSetting(String query) {
		return this.repositoryManagement.getSetting(this, query);
	}

	/**
	 * Get a list of all settings of the repository.
	 * 
	 * @return The list of settings
	 */
	public List<RepositorySetting> getSettings() {
		return this.repositoryManagement.getSettings(this);
	}

	/**
	 * Add a new setting to the repository.
	 * 
	 * @param name
	 *            The name of the setting
	 * @param value
	 *            The value of the setting
	 */
	public void addSetting(String name, String value) {
		this.repositoryManagement.addSetting(this, name, value);
	}

}