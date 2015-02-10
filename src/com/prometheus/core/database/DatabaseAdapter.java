package com.prometheus.core.database;

import org.apache.tomcat.util.descriptor.web.ContextResource;

import com.prometheus.core.configuration.Configuration;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public interface DatabaseAdapter {

	/**
	 * 
	 * @param settings
	 * @return
	 * @throws ConfigurationException
	 */
	public ContextResource createResource(Configuration settings) throws ConfigurationException;

}