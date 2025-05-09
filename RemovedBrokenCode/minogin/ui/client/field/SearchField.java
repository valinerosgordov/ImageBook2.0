package ru.minogin.ui.client.field;

import com.google.common.base.Objects;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.*;

public class SearchField extends Composite implements HasValue<String>, HasKeyUpHandlers {
	private static final int DELAY_MS = 200;

	private TextBox textBox = new TextBox();
	private Timer timer;
	private Image cancelImage;
	private String query;

	public SearchField() {
		Resources.INSTANCE.searchFieldCss().ensureInjected();

		FlowPanel flowPanel = new FlowPanel();

		textBox.getElement().setAttribute("placeHolder", "Найти");		// TODO
		flowPanel.add(textBox);

		cancelImage = new Image(Resources.INSTANCE.cancelSearch());
		cancelImage.addStyleName(Resources.INSTANCE.searchFieldCss().cancelImage());
		cancelImage.setVisible(false);
		cancelImage.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setValue(null, true);
			}
		});
		flowPanel.add(cancelImage);
		initWidget(flowPanel);

		addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				onChange();
			}
		});

		addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				onChange();
			}
		});
	}

	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
		return textBox.addValueChangeHandler(handler);
	}

	@Override
	public String getValue() {
		if (textBox.getValue().isEmpty())
			return null;

		return textBox.getValue();
	}

	@Override
	public void setValue(String value) {
		textBox.setValue(value);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		textBox.setValue(value, fireEvents);
	}

	public HandlerRegistration addSearchHandler(SearchHandler handler) {
		return addHandler(handler, SearchEvent.TYPE);
	}

	private void onChange() {
		String newQuery = getValue();
		if (Objects.equal(newQuery, query))
			return;
		query = newQuery;

		if (timer != null)
			timer.cancel();

		if (getValue() != null)
			cancelImage.setVisible(true);
		else
			cancelImage.setVisible(false);

		timer = new Timer() {
			@Override
			public void run() {
				fireEvent(new SearchEvent(getValue()));
			}
		};
		timer.schedule(DELAY_MS);
	}

	@Override
	public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
		return textBox.addKeyUpHandler(handler);
	}
}
