package com.prometheus.core.database.mssql;

import org.apache.tomcat.util.descriptor.web.ContextResource;

import com.prometheus.core.configuration.Configuration;
import com.prometheus.core.database.DatabaseAdapter;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class DatabaseAdapterMssql implements DatabaseAdapter {

	@Override
	public ContextResource createResource(Configuration settings) throws ConfigurationException {

		String name = "default";
		String type = "javax.sql.DataSource";
		String auth = "Container";
		String driverClassName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		String hostname = "localhost";
		String port = "3306";
		String username = null;
		String password = null;
		String database = null;
		String maxActive = "100";
		String maxIdle = "100";
		String maxWait = "30000";
		String initialSize = "50";
		String minIdle = "50";
		String encoding = "UTF-8";
		String defaultAutoCommit = "false";

		if ((settings.get("server.database.hostname") != null) && (!settings.get("server.database.hostname").equals(""))) {
			hostname = settings.get("server.database.hostname");
		}

		if ((settings.get("server.database.port") != null) && (!settings.get("server.database.port").equals(""))) {
			port = settings.get("server.database.port");
		}

		if ((settings.get("server.database.username") != null) && (!settings.get("server.database.username").equals(""))) {
			username = settings.get("server.database.username");
		} else {
			throw new ConfigurationException("no user specified for connection " + name);
		}

		if ((settings.get("server.database.password") != null) && (!settings.get("server.database.password").equals(""))) {
			password = settings.get("server.database.password");
		} else {
			throw new ConfigurationException("no password specified for connection " + name);
		}

		if ((settings.get("server.database.database") != null) && (!settings.get("server.database.database").equals(""))) {
			database = settings.get("server.database.database");
		} else {
			throw new ConfigurationException("no database specified for connection " + name);
		}

		if ((settings.get("server.database.maxActive") != null) && (!settings.get("server.database.maxActive").equals(""))) {
			maxActive = settings.get("server.database.maxActive");
		}

		if ((settings.get("server.database.maxIdle") != null) && (!settings.get("server.database.maxIdle").equals(""))) {
			maxIdle = settings.get("server.database.maxIdle");
		}

		if ((settings.get("server.database.maxWait") != null) && (!settings.get("server.database.maxWait").equals(""))) {
			maxWait = settings.get("server.database.maxWait");
		}

		if ((settings.get("server.database.initialSize") != null) && (!settings.get("server.database.initialSize").equals(""))) {
			initialSize = settings.get("server.database.initialSize");
		}

		if ((settings.get("server.database.minIdle") != null) && (!settings.get("server.database.minIdle").equals(""))) {
			minIdle = settings.get("server.database.minIdle");
		}

		if ((settings.get("server.database.initialSize") != null) && (!settings.get("server.database.initialSize").equals(""))) {
			initialSize = settings.get("server.database.initialSize");
		}

		if ((settings.get("server.database.minIdle") != null) && (!settings.get("server.database.minIdle").equals(""))) {
			minIdle = settings.get("server.database.minIdle");
		}

		ContextResource resource = new ContextResource();
		resource.setName(name);
		resource.setType(type);
		resource.setAuth(auth);

		resource.setProperty("username", username);
		resource.setProperty("password", password);
		resource.setProperty("driverClassName", driverClassName);

		resource.setProperty("url", "jdbc:sqlserver://" + hostname + ":" + port + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";CharacterSet=" + encoding);
		resource.setProperty("maxActive", maxActive);
		resource.setProperty("maxIdle", maxIdle);
		resource.setProperty("maxWait", maxWait);
		resource.setProperty("InitialSize", initialSize);
		resource.setProperty("minIdle", minIdle);
		resource.setProperty("defaultAutoCommit", defaultAutoCommit);

		return resource;
	}

}