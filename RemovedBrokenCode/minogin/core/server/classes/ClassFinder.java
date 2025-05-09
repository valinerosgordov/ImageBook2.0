package ru.minogin.core.server.classes;

import java.io.File;
import java.net.URL;
import java.util.*;

import ru.minogin.core.client.exception.Exceptions;

public class ClassFinder {
	private boolean classesOnly;
	private ClassLoader classLoader;

	public Collection<Class<?>> find(String packageName) {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();

		classLoader = Thread.currentThread().getContextClassLoader();
		classes.addAll(findInPackage(packageName));

		return classes;
	}
	
	public void setClassesOnly(boolean classesOnly) {
		this.classesOnly = classesOnly;
	}

	private Collection<Class<?>> findInPackage(String packageName) {
		Collection<Class<?>> classes = new ArrayList<Class<?>>();
		try {
			String path = packageName.replace(".", "/");
			Enumeration<URL> resources = classLoader.getResources(path);
			while (resources.hasMoreElements()) {
				URL url = resources.nextElement();
				File dir = new File(url.getPath());
				if (dir.exists()) {
					for (String file : dir.list()) {
						if (file.endsWith(".class")) {
							String className = packageName + "." + file.substring(0, file.length() - 6);
							Class<?> xClass = Class.forName(className);
							if (!classesOnly || !xClass.isInterface())
								classes.add(xClass);
						}
						else {
							String subPackage = packageName + "." + file;
							classes.addAll(findInPackage(subPackage));
						}
					}
				}
			}
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}

		return classes;
	}
}
