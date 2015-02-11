package com.prometheus.core.base.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.prometheus.core.base.identities.attributes.IdentityAttribute;
import com.prometheus.core.base.identities.attributes.IdentityAttributeManagement;
import com.prometheus.core.database.DatabaseConnection;
import com.prometheus.core.utils.KeyGenerator;

/**
 * 
 * @author alt
 *
 */
public class RepositoryManagement {

	/**
	 * 
	 */
	public RepositoryManagement() {

	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public Repository getRepository(String query) {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		Repository repository = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoryGet_Single"));
			statement.setString(1, query);
			statement.setString(2, query);

			result = statement.executeQuery();

			if (result.next()) {
				repository = new Repository(this);
				repository.key = result.getString("repository_key");
				repository.name = result.getString("repository_name");
				repository.attribute = attributeManagement.getAttribute(result.getString("repository_attribute"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return repository;
	}

	/**
	 * 
	 * @return
	 */
	public List<Repository> getRepository() {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		List<Repository> repositories = new ArrayList<Repository>();

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoryGet_All"));

			result = statement.executeQuery();

			if (result.next()) {
				Repository repository = new Repository(this);
				repository.key = result.getString("repository_key");
				repository.name = result.getString("repository_name");
				repository.attribute = attributeManagement.getAttribute(result.getString("repository_attribute"));

				repositories.add(repository);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return repositories;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Repository createRepository(String name) {
		return createRepository(name, "REP" + name);
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public Repository createRepository(String name, String attributeName) {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();

		if (attributeManagement.getAttribute(attributeName) == null) {

			IdentityAttribute attribute = attributeManagement.createAttribute(attributeName, 1, 255);

			Connection connection = null;
			PreparedStatement statement = null;

			try {

				connection = DatabaseConnection.createConnection("default");
				statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoryCreate"));
				statement.setString(1, KeyGenerator.generateKey());
				statement.setString(2, name);
				statement.setString(3, attribute.getKey());

				statement.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
				attribute.delete();
				DatabaseConnection.rollback(connection);
			} finally {
				DatabaseConnection.close(statement);
				DatabaseConnection.close(connection);
			}
		}

		return getRepository(name);
	}

}