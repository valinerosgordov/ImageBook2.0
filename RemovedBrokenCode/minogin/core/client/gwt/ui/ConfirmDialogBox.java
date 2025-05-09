package ru.minogin.core.client.gwt.ui;

import ru.minogin.core.client.constants.CommonConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class ConfirmDialogBox {
	private String title;
	private String text;
	private final CommonConstants commonConstants;
	private final ClickHandler yesHandler;
	private ClickHandler noHandler;

	public ConfirmDialogBox(String title, String text, CommonConstants appConstants, ClickHandler yesHandler,
			ClickHandler noHandler) {
		this.title = title;
		this.text = text;
		this.commonConstants = appConstants;
		this.yesHandler = yesHandler;
		this.noHandler = noHandler;
	}

	public ConfirmDialogBox(String title, String text, CommonConstants appConstants, ClickHandler yesHandler) {
		this(title, text, appConstants, yesHandler, null);
	}

	public void show() {
		final DialogBox box = new DialogBox(false, true);
		box.addStyleName("confirmBox");
		box.setText(title);

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("400px");
		panel.add(new HTML(text));

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setWidth("100%");

		HorizontalPanel buttonPanel = new HorizontalPanel();
		Button yesButton = new Button(commonConstants.yes(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.hide();
				if (yesHandler != null)
					yesHandler.onClick(event);
			}
		});
		yesButton.addStyleName("confirmBox-button");
		buttonPanel.add(yesButton);
		Button noButton = new Button(commonConstants.no(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				box.hide();
				if (noHandler != null)
					noHandler.onClick(event);
			}
		});
		noButton.addStyleName("confirmBox-button");
		buttonPanel.add(noButton);
		hPanel.add(buttonPanel);
		hPanel.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_CENTER);
		panel.add(hPanel);
		// panel.setCellVerticalAlignment(noButton,
		// HasVerticalAlignment.ALIGN_BOTTOM);
		box.setWidget(panel);

		box.center();
	}
}
