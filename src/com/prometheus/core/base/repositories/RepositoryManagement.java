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
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesGet_Single"));
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
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesGet_All"));

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
				statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesCreate"));
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

	/**
	 * 
	 * @param repository
	 */
	protected void reloadRepository(Repository repository) {

		IdentityAttributeManagement attributeManagement = new IdentityAttributeManagement();

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesGet_Single"));
			statement.setString(1, repository.getKey());
			statement.setString(2, repository.getKey());

			result = statement.executeQuery();

			if (result.next()) {
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
	}

	/**
	 * 
	 * @param setting
	 */
	protected void reloadSetting(RepositorySetting setting) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsGet_Single"));
			statement.setString(1, setting.getRepository().getKey());
			statement.setString(2, setting.getKey());

			result = statement.executeQuery();

			while (result.next()) {
				setting.key = result.getString("setting_key");
				setting.name = result.getString("setting_name");
				setting.value = result.getString("setting_value");
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
	 * @param repository
	 * @param name
	 */
	protected void modifyRepository(Repository repository, String name) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesModify"));
			statement.setString(1, name);
			statement.setString(2, repository.getKey());

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		reloadRepository(repository);
	}

	/**
	 * 
	 * @param repository
	 */
	protected void deleteRepository(Repository repository) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesDelete"));
			statement.setString(1, repository.getKey());

			statement.executeUpdate();

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
	 * @param repository
	 * @param query
	 * @return
	 */
	protected RepositorySetting getSetting(Repository repository, String query) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		RepositorySetting setting = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsGet_Single"));
			statement.setString(1, repository.getKey());
			statement.setString(2, query);

			result = statement.executeQuery();

			while (result.next()) {
				setting = new RepositorySetting(this);
				setting.key = result.getString("setting_key");
				setting.repository = repository;
				setting.name = result.getString("setting_name");
				setting.value = result.getString("setting_value");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return setting;
	}

	/**
	 * 
	 * @param repository
	 */
	protected List<RepositorySetting> getSettings(Repository repository) {

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		List<RepositorySetting> settings = new ArrayList<RepositorySetting>();

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsGet_All"));
			statement.setString(1, repository.getKey());

			result = statement.executeQuery();

			while (result.next()) {
				RepositorySetting setting = new RepositorySetting(this);
				setting.key = result.getString("setting_key");
				setting.repository = repository;
				setting.name = result.getString("setting_name");
				setting.value = result.getString("setting_value");

				settings.add(setting);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseConnection.close(result);
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		return settings;
	}

	/**
	 * 
	 * @param name
	 * @param value
	 */
	protected void addSetting(Repository repository, String name, String value) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsCreate"));
			statement.setString(1, KeyGenerator.generateKey());
			statement.setString(2, repository.getKey());
			statement.setString(3, name);
			statement.setString(4, value);

			statement.executeUpdate();

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
	 * @param setting
	 * @param value
	 */
	protected void modifySetting(RepositorySetting setting, String name, String value) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsModify"));
			statement.setString(1, name);
			statement.setString(2, value);
			statement.setString(3, setting.getKey());

			statement.executeUpdate();
			setting.value = value;

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}

		reloadSetting(setting);
	}

	/**
	 * 
	 * @param setting
	 */
	protected void deleteSetting(RepositorySetting setting) {

		Connection connection = null;
		PreparedStatement statement = null;

		try {

			connection = DatabaseConnection.createConnection("default");
			statement = connection.prepareStatement(DatabaseConnection.getPreparedStatement("systemRepositoriesSettingsDelete"));
			statement.setString(1, setting.getKey());

			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			DatabaseConnection.rollback(connection);
		} finally {
			DatabaseConnection.close(statement);
			DatabaseConnection.close(connection);
		}
	}

}