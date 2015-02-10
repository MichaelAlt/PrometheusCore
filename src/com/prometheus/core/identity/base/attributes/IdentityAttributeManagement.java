package com.prometheus.core.identity.base.attributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.prometheus.core.database.DatabaseConnection;
import com.prometheus.core.utils.KeyGenerator;

/**
 * 
 * @author alt
 *
 */
public class IdentityAttributeManagement {

	/**
	 * 
	 */
	public IdentityAttributeManagement() {

	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public IdentityAttribute getAttribute(String key) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		IdentityAttribute attribute = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemGetAttribute"));
			statement.setString(1, key);
			statement.setString(2, key);

			result = statement.executeQuery();

			if (result.next()) {
				attribute = new IdentityAttribute();
				attribute.key = result.getString("attribute_key");
				attribute.name = result.getString("attribute_name");
				attribute.type = result.getInt("attribute_type");
				attribute.length = result.getInt("attribute_length");
				attribute.index = result.getBoolean("attribute_index");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return attribute;
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param length
	 * @param index
	 */
	public void createAttribute(String name, int type, int length, boolean index) {

		Connection connection = null;
		PreparedStatement statement = null;
		Statement statement1 = null;
		Statement statement2 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemCreateAttribute"));
			statement.setString(1, KeyGenerator.generateKey());
			statement.setString(2, name);
			statement.setInt(3, type);
			statement.setInt(4, length);
			statement.setBoolean(5, index);

			statement.executeUpdate();

			statement1 = connection.createStatement();
			statement1.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemCreateAttribute_Modify"), name, DatabaseConnection.getDataType(type), length));

			if (index == true) {
				statement2 = connection.createStatement();
				statement2.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemCreateAttribute_Index"), name));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param length
	 * @param index
	 */
	public void modifyAttribute(IdentityAttribute attribute, String name, int type, int length, boolean index) {

		Connection connection = null;
		PreparedStatement statement = null;
		Statement statement1 = null;
		Statement statement2 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemModifyAttribute"));
			statement.setString(1, name);
			statement.setInt(2, type);
			statement.setInt(3, length);
			statement.setBoolean(4, index);
			statement.setString(5, attribute.key);

			statement.executeUpdate();

			statement1 = connection.createStatement();
			statement1.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemModifyAttribute_Modify"), name, DatabaseConnection.getDataType(type), length));

			if (index == true) {
				statement2 = connection.createStatement();
				statement2.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemModifyAttribute_Index"), name));
			} else {
				statement2 = connection.createStatement();
				statement2.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemModifyAttribute_DropIndex"), name));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}
	}

	/**
	 * 
	 * @param attribute
	 */
	public void deleteAttribute(IdentityAttribute attribute) {

		Connection connection = null;
		Statement statement = null;
		PreparedStatement statement1 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement1 = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemDeleteAttribute"));
			statement1.setString(1, attribute.key);
			statement1.executeUpdate();

			statement = connection.createStatement();
			statement.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemDeleteAttribute_Modify"), attribute.name));

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(connection);
		}
	}

}