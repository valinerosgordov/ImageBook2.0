package ru.minogin.ui.client.field;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

/** An active field for displaying and editing text strings. The field highlights
 * when mouse is over and
 * switches to edit mode upon click (focus).<br/><br/>
 * A hint (empty text) may be specified via {@link #setEmptyText(String)}
 * <br/><br/>
 * You should listen to value changes via
 * {@link #addValueChangeHandler(ValueChangeHandler)} and apply changes
 * immediately.<br/><br/>
 * 
 * @author Andrey Minogin */
public class ActiveTextBox extends Composite implements HasValue<String>,
        Focusable, HasFocusHandlers, LeafValueEditor<String> {
	private State state = State.NORMAL;
	private FocusPanel panel;
	private HTML html;
	private TextBox textBox;
	private String emptyText;
	private int emptyWidthPx = 200; // TODO should be in EM
	private int minWidthPx = 50; // TODO should be in EM
	private Integer maxWidthPx; // = 300;
	private Validator<String> validator;
	private Mode mode;

	public ActiveTextBox(String value, String emptyText,
			ValueChangeHandler<String> handler) {
		this();

		setValue(value);
		setEmptyText(emptyText);
		addValueChangeHandler(handler);
	}

	public ActiveTextBox() {
		Resources.INSTANCE.textBoxCss().ensureInjected();

		panel = new FocusPanel();
		panel.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setEditState(true);
			}
		});

		html = new HTML();
		html.addStyleName(Resources.INSTANCE.textBoxCss().html());
		html.addStyleName(Resources.INSTANCE.textBoxCss().htmlEmpty());
		html.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (state == State.NORMAL)
					setHoverState();
			}
		});
		html.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (state == State.HOVER)
					setNormalState();
			}
		});
		html.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (state == State.NORMAL || state == State.HOVER)
					setEditState(true);
			}
		});
		panel.setWidget(html);

		textBox = new TextBox() {
			@Override
			public String getValue() {
				return Strings.emptyToNull(super.getValue());
			}
		};
		textBox.addStyleName(Resources.INSTANCE.textBoxCss().textBox());
		textBox.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				textBox.selectAll();
			}
		});
		textBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (state == State.EDIT)
					setNormalState();
			}
		});
		textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				updateHtml();
				if (state == State.EDIT)
					setNormalState();
			}
		});
		textBox.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						adjustWidth();
					}
				});
			}
		});

		initWidget(panel);

		setValue(null);
		// setWidth("15em");

		setMode(Mode.NORMAL);
	}

	private void setNormalState() {
		if (mode == Mode.INPUT)
			return;

		panel.setWidget(html);
		html.removeStyleName(Resources.INSTANCE.textBoxCss().htmlHover());
		state = State.NORMAL;
	}

	private void setHoverState() {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(html);
		html.addStyleName(Resources.INSTANCE.textBoxCss().htmlHover());
		state = State.HOVER;
	}
	
	private void setEditState(boolean focus) {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(textBox);
		html.removeStyleName(Resources.INSTANCE.textBoxCss().htmlHover());
		state = State.EDIT;
		
		if (focus) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					textBox.setFocus(true);
				}
			});
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			final ValueChangeHandler<String> handler) {
		return textBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (validator == null) {
					handler.onValueChange(event);
					return;
				}

				String value = event.getValue();
				if (validator.validate(value)) {
					setInvalid(false);
					handler.onValueChange(event);
				}
				else {
					setInvalid(true);
				}
			}
		});
	}

	@Override
	public String getValue() {
		return textBox.getValue();
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		textBox.setValue(value, fireEvents);
		if (!fireEvents)
			updateHtml();
		adjustWidth();
	}

	private void updateHtml() {
		String value = getValue();
		if (value != null) {
			html.setText(value);
			html.removeStyleName(Resources.INSTANCE.textBoxCss().htmlEmpty());
		}
		else {
			if (emptyText != null) {
				html.setText(emptyText);
				html.addStyleName(Resources.INSTANCE.textBoxCss().htmlEmpty());
			}
			else
				html.setHTML("&nbsp;");
		}
	}

	private void adjustWidth() {
		int widthPx;
		String value = textBox.getValue();
		if (!Strings.isNullOrEmpty(value))
			widthPx = calculateBoxWidth(value, null);
		else {
			value = emptyText;
			if (!Strings.isNullOrEmpty(value))
				widthPx = calculateBoxWidth(value, Resources.INSTANCE.textBoxCss()
						.htmlEmpty());
			else
				widthPx = emptyWidthPx;
		}

		if (widthPx < minWidthPx)
			widthPx = minWidthPx;
		if (maxWidthPx != null && widthPx > maxWidthPx)
			widthPx = maxWidthPx;

		html.setWidth(widthPx + "px");
		textBox.setWidth(widthPx + "px");
	}

	private int calculateBoxWidth(String value, String additionalClassName) {
		SpanElement span = Document.get().createSpanElement();
		span.setClassName(Resources.INSTANCE.textBoxCss().html());
		if (additionalClassName != null)
			span.addClassName(additionalClassName);
		span.setInnerText(value);
		span.getStyle().setVisibility(Visibility.HIDDEN);
		Document.get().getBody().appendChild(span);
		int widthPx = span.getOffsetWidth() + 2;
		if (additionalClassName != null)
			widthPx += 4;	// TODO workaround for italic
		span.removeFromParent();
		return widthPx;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
		refresh();
	}

	private void refresh() {
		setValue(getValue());
	}

	@Override
	public int getTabIndex() {
		return panel.getTabIndex();
	}

	@Override
	public void setTabIndex(int index) {
		panel.setTabIndex(index);
	}

	@Override
	public void setAccessKey(char key) {
		panel.setAccessKey(key);
	}

	@Override
	public void setFocus(boolean focused) {
		panel.setFocus(focused);
	}

	// TODO should be in EM
	public void setEmptyWidthPx(int emptyWidthPx) {
		this.emptyWidthPx = emptyWidthPx;
	}

	// TODO should be in EM
	public void setMinWidthPx(int minWidthPx) {
		this.minWidthPx = minWidthPx;
	}

	/** May be null */
	// TODO should be in EM
	public void setMaxWidthPx(Integer maxWidthPx) {
		this.maxWidthPx = maxWidthPx;
	}

	@Override
	public HandlerRegistration addFocusHandler(FocusHandler handler) {
		return panel.addFocusHandler(handler);
	}

	public void setInvalid(boolean invalid) {
		html.setStyleName(Resources.INSTANCE.textBoxCss().htmlInvalid(), invalid);
	}

	/** Enables validation. If value is invalid a field is marked as invalid (with
	 * a red border)
	 * and {@link ValueChangeEvent} is not fired. */
	public void setValidator(Validator<String> validator) {
		this.validator = validator;
	}

	public void setMode(Mode mode) {
		this.mode = mode;

		if (mode == Mode.READONLY) {
			html.getElement().getStyle().setCursor(Cursor.TEXT);
			setNormalState();
		}
		else if (mode == Mode.NORMAL) {
			html.getElement().getStyle().setCursor(Cursor.POINTER);
		}
		else if (mode == Mode.INPUT) {
			setEditState(false);
		}
	}
}
