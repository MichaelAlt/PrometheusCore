package com.prometheus.core.base.identities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.prometheus.core.base.identities.attributes.IdentityAttribute;
import com.prometheus.core.base.identities.attributes.IdentityAttributeManagement;
import com.prometheus.core.database.DatabaseConnection;
import com.prometheus.core.utils.KeyGenerator;

/**
 * 
 * @author alt
 *
 */
public class IdentityManagement {

	/**
	 * 
	 * @param key
	 */
	public Identity getIdentity(String key) {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();
		List<IdentityAttribute> attributeList = attributeManagement.getAttributes();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		Identity identity = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemIdentitiesGet_Single"));
			statement.setString(1, key);

			result = statement.executeQuery();

			if (result.next()) {
				identity = new Identity();
				identity.key = result.getString("identity_key");
				identity.created = result.getTimestamp("identity_created");
				identity.changed = result.getTimestamp("identity_changed");

				for (IdentityAttribute attribute : attributeList) {
					System.out.println(attribute.getName());
					identity.attributes.put(attribute.getName(), result.getString(attribute.getName()));
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return identity;
	}

	/**
	 * 
	 * @param key
	 */
	public List<Identity> getIdentity() {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();
		List<IdentityAttribute> attributeList = attributeManagement.getAttributes();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		List<Identity> identities = new ArrayList<Identity>();

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemIdentitiesGet_All"));

			result = statement.executeQuery();

			while (result.next()) {
				Identity identity = new Identity();
				identity.key = result.getString("identity_key");
				identity.created = result.getTimestamp("identity_created");
				identity.changed = result.getTimestamp("identity_changed");

				for (IdentityAttribute attribute : attributeList) {
					identity.attributes.put(attribute.getName(), result.getString(attribute.getName()));
				}

				identities.add(identity);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return identities;
	}

	/**
	 * 
	 * @param attributes
	 */
	public void createIdentity(HashMap<String, String> attributes) {

		Connection connection = null;
		Statement statement = null;

		String attributeNames = "identity_key,";
		String attributeValues = "'" + KeyGenerator.generateKey() + "',";

		for (Entry<String, String> attribute : attributes.entrySet()) {
			attributeNames += attribute.getKey() + ",";
			attributeValues += "'" + attribute.getValue() + "',";
		}

		attributeNames = attributeNames.substring(0, attributeNames.length() - 1);
		attributeValues = attributeValues.substring(0, attributeValues.length() - 1);

		try {

			System.out.println(String.format("INSERT INTO prometheus_identities(%s) VALUES(%S)", attributeNames, attributeValues));

			connection = DatabaseConnection.createConnection("default");
			statement = connection.createStatement();
			statement.executeUpdate(String.format("INSERT INTO prometheus_identities(%s) VALUES(%S)", attributeNames, attributeValues));

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}
	}

}