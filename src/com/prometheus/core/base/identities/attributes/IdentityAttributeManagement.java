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
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemAttributeGet_Single"));
			statement.setString(1, key);
			statement.setString(2, key);

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
	 * 
	 * @return
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
	 * 
	 * @param name
	 * @param type
	 * @param length
	 * @return
	 */
	public IdentityAttribute createAttribute(String name, int type, int length) {
		return this.createAttribute(name, type, length, true, null);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param length
	 * @param nullable
	 * @return
	 */
	public IdentityAttribute createAttribute(String name, int type, int length, boolean nullable) {
		return this.createAttribute(name, type, length, nullable, null);
	}

	/**
	 * 
	 * @param name
	 * @param type
	 * @param length
	 * @param nullable
	 * @param defaultValue
	 * @return
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
	 * 
	 * @param attribute
	 * @param name
	 * @param type
	 * @param length
	 * @param nullable
	 * @param defaultValue
	 */
	protected void modifyAttribute(IdentityAttribute attribute, String name, int type, int length, boolean nullable, String defaultValue) {

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
	}

	/**
	 * 
	 * @param attribute
	 */
	protected void deleteAttribute(IdentityAttribute attribute) {

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