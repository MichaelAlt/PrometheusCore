package com.prometheus.core.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author michi
 *
 */
public class ConfigurationContext {

	private static Configuration applicationConfiguration;

	/**
	 * 
	 */
	static {

		try {
			applicationConfiguration = new Configuration();
			loadConnectionConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * @throws ConfigurationException
	 * 
	 */
	private static void loadConnectionConfiguration() throws ConfigurationException {

		File file = new File("conf/connection.ini");

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

					} else {
						throw new ConfigurationException("Error while reading configuration file: " + lineCount + ":" + line);
					}
				}

				lineCount += 1;
			}

		} catch (FileNotFoundException e) {
			throw new ConfigurationException("configuration file not found: " + file.getAbsolutePath());
		} catch (IOException e) {
			throw new ConfigurationException("Error while reading configuration file: " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key) {
		return applicationConfiguration.get(key);
	}

}