package ru.minogin.core.client.gwt;

import java.util.MissingResourceException;

import com.google.gwt.i18n.client.Dictionary;

public class ClientParametersReader {
	private Dictionary dictionary;

	public ClientParametersReader() {
		try {
			dictionary = Dictionary.getDictionary("clientParameters");
		}
		catch (MissingResourceException e) {
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getParam(String name) {
		if (dictionary == null)
			return null;

		String s;
		try {
			s = dictionary.get(name);
		}
		catch (MissingResourceException e) {
			return null;
		}

		char t = s.charAt(0);
		s = s.substring(2);
		Object value;
		if (t == 's')
			value = s;
		else if (t == 'i')
			value = new Integer(s);
		else if (t == 'b')
			value = new Boolean(s);
		else
			throw new RuntimeException("Unsupported type: " + t);

		return (T) value;
	}

	public boolean hasParam(String name) {
		return getParam(name) != null;
	}
}
