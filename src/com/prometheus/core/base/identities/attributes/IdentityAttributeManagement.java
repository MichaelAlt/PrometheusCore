package com.prometheus.core.base.identities.attributes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.prometheus.core.database.DatabaseConnection;
import com.prometheus.core.utils.KeyGenerator;

/**
 * This class manages the creation, modification and deletion of identity
 * attributes.
 * 
 * @author alt
 *
 */
public class IdentityAttributeManagement {

	/**
	 * Default constructor of the class.
	 * 
	 */
	public IdentityAttributeManagement() {

	}

	/**
	 * Get an attribute with a specific key or name.
	 * 
	 * @param query
	 *            The key or name of the attribute
	 * @return The attribute
	 */
	public IdentityAttribute getAttribute(String query) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		IdentityAttribute attribute = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeGet_Single"));
			statement.setString(1, query);
			statement.setString(2, query);

			result = statement.executeQuery();

			if (result.next()) {
				attribute = new IdentityAttribute(this);
				attribute.key = result.getString("attribute_key");
				attribute.name = result.getString("attribute_name");
				attribute.type = result.getInt("attribute_type");
				attribute.length = result.getInt("attribute_length");
				attribute.nullable = result.getBoolean("attribute_nullable");
				attribute.defaultValue = result.getString("attribute_default");
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
	 * Get a list of all attributes.
	 * 
	 * @return The list of attributes
	 */
	public List<IdentityAttribute> getAttributes() {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		List<IdentityAttribute> attributes = new ArrayList<IdentityAttribute>();

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeGet_All"));

			result = statement.executeQuery();

			while (result.next()) {
				IdentityAttribute attribute = new IdentityAttribute(this);
				attribute.key = result.getString("attribute_key");
				attribute.name = result.getString("attribute_name");
				attribute.type = result.getInt("attribute_type");
				attribute.length = result.getInt("attribute_length");
				attribute.nullable = result.getBoolean("attribute_nullable");
				attribute.defaultValue = result.getString("attribute_default");

				attributes.add(attribute);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return attributes;
	}

	/**
	 * Create a new identity attribute.
	 * 
	 * @param name
	 *            The name of the attribute, must be unique
	 * @param type
	 *            The type of the attribute (VARCHAR,INT etc.)
	 * @param length
	 *            The length of the attribute
	 * @return The newly created attribute
	 */
	public IdentityAttribute createAttribute(String name, int type, int length) {
		return this.createAttribute(name, type, length, true, null);
	}

	/**
	 * Reload an existing attribute to get it's new values from the database.
	 * 
	 * @param attribute
	 *            The attribute to reload
	 */
	protected void reloadAttribute(IdentityAttribute attribute) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeGet_Single"));
			statement.setString(1, attribute.getKey());
			statement.setString(2, attribute.getKey());

			result = statement.executeQuery();

			while (result.next()) {
				attribute.key = result.getString("attribute_key");
				attribute.name = result.getString("attribute_name");
				attribute.type = result.getInt("attribute_type");
				attribute.length = result.getInt("attribute_length");
				attribute.nullable = result.getBoolean("attribute_nullable");
				attribute.defaultValue = result.getString("attribute_default");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}
	}

	/**
	 * Create a new identity attribute
	 * 
	 * @param name
	 *            The name of the attribute, must be unique
	 * @param type
	 *            The type of the attribute (VARCHAR,INT etc.)
	 * @param length
	 *            The length of the attribute
	 * @param nullable
	 *            Can the value of the attribute be null (true, false)
	 * @return The newly created attribute
	 */
	public IdentityAttribute createAttribute(String name, int type, int length, boolean nullable) {
		return this.createAttribute(name, type, length, nullable, null);
	}

	/**
	 * Create a new identity attribute
	 * 
	 * @param name
	 *            The name of the attribute, must be unique
	 * @param type
	 *            The type of the attribute (VARCHAR,INT etc.)
	 * @param length
	 *            The length of the attribute
	 * @param nullable
	 *            Can the value of the attribute be null (true, false)
	 * @param defaultValue
	 *            Default value of the attribute if no value is set
	 * @return The newly created attribute
	 */
	public IdentityAttribute createAttribute(String name, int type, int length, boolean nullable, String defaultValue) {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeCreate"));
			statement.setString(1, KeyGenerator.generateKey());
			statement.setString(2, name);
			statement.setInt(3, type);
			statement.setInt(4, length);
			statement.setBoolean(5, nullable);
			statement.setString(6, defaultValue);

			statement.executeUpdate();

			if (nullable == true) {

				if (defaultValue == null) {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeCreate_ModifyTable_Null"), name, DatabaseConnection.getDataType(type), length));
					statement1.executeUpdate();
				} else {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeCreate_ModifyTable_Null_Default"), name, DatabaseConnection.getDataType(type), length));
					statement1.setString(1, defaultValue);

					statement1.executeUpdate();
				}
			} else {

				if (defaultValue == null) {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeCreate_ModifyTable_NotNull"), name, DatabaseConnection.getDataType(type), length));
					statement1.executeUpdate();
				} else {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeCreate_ModifyTable_NotNull_Default"), name, DatabaseConnection.getDataType(type), length));
					statement1.setString(1, defaultValue);

					statement1.executeUpdate();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(statement1);
			DatabaseConnection.close(connection);
		}

		return this.getAttribute(name);
	}

	/**
	 * Modify the setting for an attribute field.
	 * 
	 * @param attribute
	 *            The attribute to modify
	 * @param name
	 *            The new name of the attribute
	 * @param type
	 *            The new type of the attribute
	 * @param length
	 *            The new length of the attribute
	 * @param nullable
	 *            Is the attribute now nullable (true, false)
	 * @param defaultValue
	 *            Th enew default value of the attribute
	 */
	protected void modify(IdentityAttribute attribute, String name, int type, int length, boolean nullable, String defaultValue) {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeModify"));
			statement.setString(1, KeyGenerator.generateKey());
			statement.setString(2, name);
			statement.setInt(3, type);
			statement.setInt(4, length);
			statement.setBoolean(5, nullable);
			statement.setString(6, defaultValue);

			statement.executeUpdate();

			if (nullable == true) {

				if (defaultValue == null) {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeModify_ModifyTable_Null"), name, DatabaseConnection.getDataType(type), length));
					statement1.executeUpdate();
				} else {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeModify_ModifyTable_Null_Default"), name, DatabaseConnection.getDataType(type), length));
					statement1.setString(1, defaultValue);

					statement1.executeUpdate();
				}
			} else {

				if (defaultValue == null) {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeModify_ModifyTable_NotNull"), name, DatabaseConnection.getDataType(type), length));
					statement1.executeUpdate();
				} else {
					statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeModify_ModifyTable_NotNull_Default"), name, DatabaseConnection.getDataType(type), length));
					statement1.setString(1, defaultValue);

					statement1.executeUpdate();
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(statement1);
			DatabaseConnection.close(connection);
		}

		reloadAttribute(attribute);
	}

	/**
	 * Delete the specified attribute.
	 * 
	 * @param attribute
	 *            The attribute to delete
	 */
	protected void delete(IdentityAttribute attribute) {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeDelete"));
			statement.setString(1, attribute.key);
			statement.executeUpdate();

			statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemAttributeDelete_ModifyTable"), attribute.name));
			statement1.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(statement1);
			DatabaseConnection.close(connection);
		}
	}

}