package com.prometheus.core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseConnection {

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
			// CompanyCloudLogging.logError(Level.SEVERE, e.getMessage());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
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
			// CompanyCloudLogging.logError(Level.SEVERE, e.getMessage());
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
			// CompanyCloudLogging.logError(Level.SEVERE, e.getMessage());
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
			// CompanyCloudLogging.logError(Level.SEVERE, e.getMessage());
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
			// CompanyCloudLogging.logError(Level.SEVERE, e.getMessage());
		}
	}

}
