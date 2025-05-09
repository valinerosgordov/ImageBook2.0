package ru.imagebook.client.flash.view;

import com.google.gwt.i18n.client.Messages;

public interface FlashMessages extends Messages {
	String flashPublished(String url);

	String widthInfo(int min, int max);

	String tooManyFlashes(int maxFlashes);
}
