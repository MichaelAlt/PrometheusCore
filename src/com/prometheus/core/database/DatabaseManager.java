package com.prometheus.core.database;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;

import com.prometheus.core.configuration.ConfigurationContext;
import com.prometheus.core.database.mssql.DatabaseAdapterMssql;
import com.prometheus.core.database.mysql.DatabaseAdapterMysql;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class DatabaseManager {

	/**
	 * 
	 * @param context
	 * @throws ConfigurationException
	 * @throws LifecycleException
	 */
	public void getConnections(Context context) throws ConfigurationException, LifecycleException {

		DatabaseAdapter adapter = null;

		if (ConfigurationContext.get("server.database.type").equals("mysql")) {
			adapter = new DatabaseAdapterMysql();
			DatabaseConnection.loadStatementStrings("mysql");
		} else if (ConfigurationContext.get("server.database.type").equals("mssql")) {
			adapter = new DatabaseAdapterMssql();
			DatabaseConnection.loadStatementStrings("mssql");
		}

		context.getNamingResources().addResource(adapter.createResource(ConfigurationContext.getConfiguration()));
	}

}