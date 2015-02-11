package com.prometheus.core.base.repositories;

import com.prometheus.core.base.identities.attributes.IdentityAttribute;

/**
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
	 * 
	 * @param repositoryManagement
	 */
	protected Repository(RepositoryManagement repositoryManagement) {
		this.repositoryManagement = repositoryManagement;
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public IdentityAttribute getAttribute() {
		return attribute;
	}

}