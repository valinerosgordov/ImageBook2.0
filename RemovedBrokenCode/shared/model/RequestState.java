package ru.imagebook.shared.model;

import java.util.HashMap;
import java.util.Map;

public class RequestState {
	public static final int REQUEST = 1;
	public static final int ACT = 2;
	
	public static final Map<Integer, String> values = new HashMap<Integer, String>();

	static {
		values.put(REQUEST, "Заявка");
		values.put(ACT, "Акт");
	}
}
