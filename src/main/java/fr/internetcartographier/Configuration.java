package fr.internetcartographier;

import java.io.IOException;
import java.util.Properties;

/**
 * Singleton class for managing properties loaded from a configuration file.
 * Provides access to specific properties such as URLs.
 */
public class Configuration {

	private static Configuration instance;
	private static Properties properties;

	/**
	 * Gets the singleton instance of PropertiesSingleton.
	 *
	 * @return           The singleton instance.
	 * @throws IOException If there is an issue creating the instance.
	 */
	public static Configuration getInstance() throws IOException {
		if (instance == null) {
			instance = new Configuration();
		}

		return instance;
	}

	/**
	 * Constructs a new PropertiesSingleton and loads properties from the
	 * configuration file.
	 *
	 * @throws IOException If the configuration file is not found.
	 * @throws IOException           If there is an issue reading the configuration
	 *                               file.
	 */
	private Configuration() throws IOException {
		properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream("resources/config/config.properties"));
	}

	public String getProperty(String property) {
		return properties.getProperty(property);
	}

}
