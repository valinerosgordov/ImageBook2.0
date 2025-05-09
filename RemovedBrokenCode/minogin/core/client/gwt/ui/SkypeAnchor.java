package ru.minogin.core.client.gwt.ui;

import com.google.gwt.user.client.ui.Anchor;

public class SkypeAnchor extends Anchor {
	public SkypeAnchor(String skype) {
		super(skype, "skype:" + skype, "_blank");
	}
}
