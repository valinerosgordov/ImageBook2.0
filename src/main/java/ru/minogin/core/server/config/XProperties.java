package ru.minogin.core.server.config;

import java.io.FileInputStream;
import java.util.Properties;

import ru.minogin.core.client.exception.Exceptions;

public class XProperties extends Properties {
	private static final long serialVersionUID = 6324219175396412060L;

	public XProperties(String path) {
		try {
			FileInputStream in = new FileInputStream(path);
			load(in);
			in.close();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
