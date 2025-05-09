package ru.minogin.core.client.gwt.ui;

import com.google.gwt.user.client.ui.Anchor;

public class EmailAnchor extends Anchor {
	public EmailAnchor(String email) {
		super(email, "mailto:" + email, "_blank");
	}
}
