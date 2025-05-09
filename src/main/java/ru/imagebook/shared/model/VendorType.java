package ru.imagebook.shared.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class VendorType {
	public static final int IMAGEBOOK = 1;
	public static final int VENDOR = 2;

	public static final Map<Integer, String> values = new LinkedHashMap<Integer, String>();

	static {
		values.put(IMAGEBOOK, "Imagebook");
		values.put(VENDOR, "Вендор");
	}
}
