package ru.minogin.core.client.gwt.ui;

import ru.minogin.core.client.util.PhoneUtil;

import com.google.gwt.user.client.ui.Anchor;

public class SkypePhoneAnchor extends Anchor {
	public SkypePhoneAnchor(String phone) {
		super(phone, "skype:" + PhoneUtil.skype(phone), "_blank");
	}
}
