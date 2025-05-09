package ru.minogin.core.server.config;

import java.util.Properties;

public class PropertiesConfig {
	private final Properties properties;

	public PropertiesConfig(Properties properties) {
		this.properties = properties;
	}

	protected String get(String name) {
		String value = properties.getProperty(name);

		if (value == null)
			throw new RuntimeException("Missing config property: " + name);

		return value;
	}
}
