package com.prometheus.core.database.mysql;

import org.apache.tomcat.util.descriptor.web.ContextResource;

import com.prometheus.core.configuration.Configuration;
import com.prometheus.core.database.DatabaseAdapter;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class DatabaseAdapterMysql implements DatabaseAdapter {

	@Override
	public ContextResource createResource(Configuration settings) throws ConfigurationException {

		String name = "default";
		String type = "javax.sql.DataSource";
		String auth = "Container";
		String driverClassName = "com.mysql.jdbc.Driver";
		String username = null;
		String password = null;
		String hostname = "localhost";
		String database = null;
		String encoding = "utf8";
		String port = "3306";
		String maxActive = "10";
		String maxIdle = "5";
		String maxWait = "10000";
		String defaultAutoCommit = "false";

		if ((settings.get("adapter.username") != null) && (!settings.get("adapter.username").equals(""))) {
			username = settings.get("adapter.username");
		} else {
			throw new ConfigurationException("no user specified for connection " + name);
		}

		if ((settings.get("adapter.password") != null) && (!settings.get("adapter.password").equals(""))) {
			password = settings.get("adapter.password");
		} else {
			throw new ConfigurationException("no password specified for connection " + name);
		}

		if ((settings.get("adapter.hostname") != null) && (!settings.get("adapter.hostname").equals(""))) {
			hostname = settings.get("adapter.hostname");
		}

		if ((settings.get("adapter.database") != null) && (!settings.get("adapter.database").equals(""))) {
			database = settings.get("adapter.database");
		} else {
			throw new ConfigurationException("no database specified for connection " + name);
		}

		if ((settings.get("adapter.encoding") != null) && (!settings.get("adapter.encoding").equals(""))) {
			encoding = settings.get("adapter.encoding");
		}

		if ((settings.get("adapter.port") != null) && (!settings.get("adapter.port").equals(""))) {
			port = settings.get("adapter.port");
		}

		if ((settings.get("adapter.pool") != null) && (!settings.get("adapter.pool").equals(""))) {
			maxActive = settings.get("adapter.pool");
		}

		if ((settings.get("adapter.idle") != null) && (!settings.get("adapter.idle").equals(""))) {
			maxIdle = settings.get("adapter.idle");
		}

		if ((settings.get("adapter.wait") != null) && (!settings.get("adapter.wait").equals(""))) {
			maxWait = settings.get("adapter.wait");
		}

		if ((settings.get("adapter.autocommit") != null) && (!settings.get("adapter.autocommit").equals(""))) {
			defaultAutoCommit = (String) settings.get("adapter.autocommit");
		}

		ContextResource resource = new ContextResource();
		resource.setName(name);
		resource.setType(type);
		resource.setAuth(auth);

		resource.setProperty("username", username);
		resource.setProperty("password", password);
		resource.setProperty("driverClassName", driverClassName);

		resource.setProperty("url", "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?characterEncoding=" + encoding);
		resource.setProperty("maxActive", maxActive);
		resource.setProperty("maxIdle", maxIdle);
		resource.setProperty("maxWait", maxWait);
		resource.setProperty("defaultAutoCommit", defaultAutoCommit);

		return resource;
	}

}