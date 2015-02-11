package com.prometheus.core.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.prometheus.core.exception.ConfigurationException;

/**
 * 
 * @author alt
 *
 */
public class DatabaseConnection {

	private static String databaseType;
	private static HashMap<String, String> databaseStatements;
	private static HashMap<Integer, String> databaseDataTypes;

	/**
	 * 
	 * @param type
	 * @throws ConfigurationException
	 */
	protected static void loadStatementStrings(String type) throws ConfigurationException {

		DatabaseConnection.databaseType = type;
		DatabaseConnection.databaseStatements = new HashMap<String, String>();
		DatabaseConnection.databaseDataTypes = new HashMap<Integer, String>();

		DatabaseConnection.databaseDataTypes.put(1, "VARCHAR");
		DatabaseConnection.databaseDataTypes.put(2, "INT");
		DatabaseConnection.databaseDataTypes.put(3, "DOUBLE");

		File directory = new File(System.getProperty("user.dir") + "/db/" + DatabaseConnection.databaseType);
		DatabaseConnection.loadStatementFiles(directory);
	}

	/**
	 * Create a new connection to the datasource with the specified name.
	 * 
	 * @param name
	 *            The name of the datasource zu load
	 * @return a new database connection
	 * @throws NamingException
	 * @throws SQLException
	 */
	public static Connection createConnection(String name) {

		Connection connection = null;

		try {

			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup(name);

			connection = ds.getConnection();
			connection.setAutoCommit(false);

		} catch (SQLException e) {
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return connection;
	}

	/**
	 * Close and commit all changes to a specified database.
	 * 
	 * @param connection
	 *            The connection to a database
	 */
	public static void close(Connection connection) {

		try {

			if (connection != null) {
				connection.commit();
				connection.close();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * Safely close a generated Statement for a database connection.
	 * 
	 * @param statement
	 *            The specified Statement
	 */
	public static void close(PreparedStatement statement) {

		try {

			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * 
	 * @param statement
	 */
	public static void close(Statement statement) {

		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {

		}
	}

	/**
	 * Safely close a generated ResultSet for a database connection.
	 * 
	 * @param result
	 *            The specified ResultSet
	 */
	public static void close(ResultSet result) {

		try {

			if (result != null) {
				result.close();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * Revert all changes made for the current database connection.
	 * 
	 * @param connection
	 *            The connection to reset
	 */
	public static void rollback(Connection connection) {

		try {
			if (connection != null) {
				connection.rollback();
			}
		} catch (SQLException e) {
		}
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public static String getPreparedStatement(String name) throws SQLException {

		String statement = DatabaseConnection.databaseStatements.get(name.toLowerCase());

		if (statement != null) {
			return statement;
		} else {
			throw new SQLException("No statement found: " + name);
		}
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String getDataType(int type) {

		return DatabaseConnection.databaseDataTypes.get(type);
	}

	/**
	 * 
	 * @param file
	 * @throws ConfigurationException
	 */
	private static void loadStatementFiles(File file) throws ConfigurationException {

		for (File statementFile : file.listFiles()) {

			if (statementFile.isDirectory()) {
				DatabaseConnection.loadStatementFiles(statementFile);
			} else {

				if (statementFile.getName().endsWith(".sql")) {

					String line = null;
					StringBuilder builder = new StringBuilder();
					BufferedReader reader = null;

					try {

						reader = new BufferedReader(new FileReader(statementFile));

						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							reader.close();
						} catch (Exception e) {
						}
					}

					String name = statementFile.getName().replace(".sql", "");
					name = name.toLowerCase();

					if (!DatabaseConnection.databaseStatements.containsKey(name)) {
						DatabaseConnection.databaseStatements.put(name, builder.toString());
					} else {
						throw new ConfigurationException();
					}
				}
			}
		}
	}

}