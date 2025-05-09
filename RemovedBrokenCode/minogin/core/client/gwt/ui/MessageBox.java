package ru.minogin.core.client.gwt.ui;

import ru.minogin.core.client.constants.CommonConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageBox {
	private String title;
	private String text;
	private final CommonConstants appConstants;
	private MessageBoxListener listener;
	private boolean showButton = true;

	public MessageBox(String title, String text, CommonConstants appConstants) {
		this.title = title;
		this.text = text;
		this.appConstants = appConstants;
	}

	public void show() {
		final DialogBox box = new DialogBox(false, true);
		box.setText(title);

		VerticalPanel panel = new VerticalPanel();
		panel.setWidth("300px");
		panel.add(new HTML(text));

		if (showButton) {
			Button button = new Button(appConstants.ok(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					box.hide();

					if (listener != null)
						listener.onOK();
				}
			});
			panel.add(button);
			panel.setCellHorizontalAlignment(button, HasHorizontalAlignment.ALIGN_CENTER);
			panel.setCellVerticalAlignment(button, HasVerticalAlignment.ALIGN_BOTTOM);
		}

		box.setWidget(panel);

		box.center();
	}

	public void setListener(MessageBoxListener listener) {
		this.listener = listener;
	}

	public void setShowButton(boolean showButton) {
		this.showButton = showButton;
	}
}
