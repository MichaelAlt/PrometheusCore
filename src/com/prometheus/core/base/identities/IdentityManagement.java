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
			statement.setString(2, key);

			result = statement.executeQuery();

			if (result.next()) {
				identity = new Identity(this);
				identity.key = result.getString("identity_key");
				identity.unique = result.getString("identity_unique");
				identity.created = result.getTimestamp("identity_created");
				identity.changed = result.getTimestamp("identity_changed");

				for (IdentityAttribute attribute : attributeList) {
					identity.attributes.put(attribute.getName().toLowerCase(), result.getString(attribute.getName()));
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
	public List<Identity> getIdentities() {

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
				Identity identity = new Identity(this);
				identity.unique = result.getString("identity_unique");
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
	public Identity createIdentity(String uniqueName, HashMap<String, String> attributes) {

		Connection connection = null;
		Statement statement = null;

		String identityKey = KeyGenerator.generateKey();

		String attributeNames = "identity_key,identity_unique,identity_created,";
		String attributeValues = "'" + identityKey + "','" + uniqueName + "',CURRENT_TIMESTAMP(),";

		for (Entry<String, String> attribute : attributes.entrySet()) {
			attributeNames += attribute.getKey() + ",";
			attributeValues += "'" + attribute.getValue() + "',";
		}

		attributeNames = attributeNames.substring(0, attributeNames.length() - 1);
		attributeValues = attributeValues.substring(0, attributeValues.length() - 1);

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.createStatement();
			statement.executeUpdate(String.format(DatabaseConnection.getPreparedStatement("systemIdentitiesCreate"), attributeNames, attributeValues));

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return getIdentity(identityKey);
	}

	/**
	 * 
	 * @param identity
	 */
	protected void reloadIdentity(Identity identity) {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();
		List<IdentityAttribute> attributeList = attributeManagement.getAttributes();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemIdentitiesGet_Single"));
			statement.setString(1, identity.getKey());

			result = statement.executeQuery();

			if (result.next()) {
				identity.key = result.getString("identity_key");
				identity.unique = result.getString("identity_unique");
				identity.created = result.getTimestamp("identity_created");
				identity.changed = result.getTimestamp("identity_changed");

				for (IdentityAttribute attribute : attributeList) {
					identity.attributes.put(attribute.getName().toLowerCase(), result.getString(attribute.getName()));
				}
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
	 *  
	 * @param identity
	 * @param name
	 * @param value
	 */
	protected void changeAttribute(Identity identity, String name, String value) {

		Connection connection = null;
		PreparedStatement statement = null;
		PreparedStatement statement1 = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemIdentitiesChange_History"));
			statement.setString(1, KeyGenerator.generateKey());
			statement.setString(2, identity.getKey());
			statement.setString(3, name);
			statement.setString(4, identity.getAttribute(name));
			statement.executeUpdate();

			statement1 = connection.prepareStatement(String.format(DatabaseConnection.getPreparedStatement("systemIdentitiesChange"), name));
			statement1.setString(1, value);
			statement1.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(statement1);
			DatabaseConnection.close(connection);
		}

		reloadIdentity(identity);
	}

	/**
	 * 
	 * @param identity
	 * @param attributes
	 */
	protected void changeAttributes(Identity identity, HashMap<String, String> attributes) {

		for (Entry<String, String> entry : attributes.entrySet()) {
			changeAttribute(identity, entry.getKey(), entry.getValue());
		}
	}

}