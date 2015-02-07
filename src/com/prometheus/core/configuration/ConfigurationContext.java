package com.prometheus.core.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.prometheus.core.database.DatabaseConnection;
import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class ConfigurationContext {

	private static Configuration applicationConfiguration = new Configuration();

	/**
	 * 
	 */
	static {

		try {
			loadConfigurationFile();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 
	 * @throws ConfigurationException
	 */
	private static void loadConfigurationFile() throws ConfigurationException {

		File file = new File(System.getProperty("user.dir") + "/conf/connection.ini");

		BufferedReader reader = null;
		String line = null;
		int lineCount = 1;

		try {

			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {

				line = line.trim();
				if ((!line.startsWith("#")) && (!line.startsWith(";")) && (!line.equals(""))) {

					String[] entry = line.split("=");

					if (entry.length == 2) {
						applicationConfiguration.put(entry[0].trim(), entry[1].trim());
					} else {
						throw new ConfigurationException("Error while reading configuration file: " + lineCount + ":" + line);
					}
				}

				lineCount += 1;
			}

		} catch (FileNotFoundException e) {
			throw new ConfigurationException("Error configuration file not found: " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new ConfigurationException("Error while reading configuration file: " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 
	 * @throws ConfigurationException
	 */
	public static void loadConfiguration() throws ConfigurationException {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement("SELECT * FROM prometheus_configuration");

			result = statement.executeQuery();

			while (result.next()) {
				applicationConfiguration.put(result.getString("entry_name"), result.getString("entry_value"));
			}

		} catch (SQLException e) {
			throw new ConfigurationException("Error, could not read configuration from database: " + e.getMessage());
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String get(String name) {
		return applicationConfiguration.get(name);
	}

	/**
	 * 
	 * @return
	 */
	public static Configuration getConfiguration() {
		return applicationConfiguration;
	}

}