package ru.minogin.core.server.log4j;

import org.apache.log4j.PropertyConfigurator;

public class Log4JConfigurer {
	public void configure(String configPathEnvVar) {
		String configPath = System.getProperty(configPathEnvVar);
		if (configPath == null)
			configPath = System.getenv(configPathEnvVar);
		PropertyConfigurator.configure(configPath + "/log4j.properties");
	}
}
