package ru.minogin.core.client.ui;

import com.google.gwt.user.client.DOM;

public class XDOM {
	public static boolean exists(String elementId) {
		return DOM.getElementById(elementId) != null;
	}
}
