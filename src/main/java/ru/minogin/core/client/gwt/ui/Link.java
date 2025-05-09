package ru.minogin.core.client.gwt.ui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

public class Link extends Anchor {
	public Link(String text, final ClickHandler handler) {
		super(text);
		
		addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				handler.onClick(event);
				
				event.preventDefault();
			}
		});
	}
}
