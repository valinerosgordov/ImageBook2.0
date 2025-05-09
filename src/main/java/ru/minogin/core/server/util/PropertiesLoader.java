package ru.minogin.core.server.util;

import java.io.FileInputStream;
import java.util.Properties;

import ru.minogin.core.client.exception.Exceptions;

public class PropertiesLoader {
	public static Properties load(String path) {
		try {
			Properties properties = new Properties();
			FileInputStream in = new FileInputStream(path);
			properties.load(in);
			in.close();
			return properties;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}
}
