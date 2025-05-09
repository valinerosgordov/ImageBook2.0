package ru.minogin.core.server;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

public class ServiceLogger {
	private static Logger logger = Logger.getLogger(ServiceLogger.class);

	public static void log(Throwable e) {
		StringWriter writer = new StringWriter();
		e.printStackTrace(new PrintWriter(writer));

		logger.error(writer.toString());
	}

	public static void warn(String message) {
		logger.warn(message);
	}
}
