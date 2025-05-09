package ru.minogin.ui.client.field;

import com.google.common.base.Strings;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.*;

/** An active field similar to {@link ActiveTextBox} for displaying and editing
 * of multiline texts without formatting.
 * <br/><br/>
 * You should listen to value changes via
 * {@link #addValueChangeHandler(ValueChangeHandler)} and apply changes
 * immediately.<br/><br/>
 * 
 * @author Andrey Minogin
 * @see ActiveTextBox */
public class ActiveTextArea extends Composite implements HasValue<String>,
        Focusable, LeafValueEditor<String> {
	private ActiveTextAreaCssResource css = Resources.INSTANCE.textAreaCss();

	private State state = State.NORMAL;
	private FocusPanel panel;
	private HTML html;
	private TextArea textArea;
	private String emptyText;
	private int emptyWidthPx = 200; // TODO should be in EM
	private int emptyHeightPx = 20; // TODO should be in EM
	private int minWidthPx = 50; // TODO should be in EM
	private Integer maxWidthPx; // = 300;
	private Validator<String> validator;
	private Mode mode;
	
	public ActiveTextArea(String value, String emptyText, ValueChangeHandler<String> handler) {
		this();

		setValue(value);
		setEmptyText(emptyText);
		addValueChangeHandler(handler);
	}

	public ActiveTextArea() {
		css.ensureInjected();

		panel = new FocusPanel();
		panel.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setEditState(true);
			}
		});

		html = new HTML();
		html.addStyleName(css.html());
		html.addStyleName(css.htmlEmpty());
		html.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (state == State.NORMAL)
					setHighlightState();
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

		textArea = new TextArea() {
			@Override
			public String getValue() {
				return Strings.emptyToNull(super.getValue());
			}
		};
		textArea.addStyleName(css.textArea());
		textArea.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				textArea.selectAll();
			}
		});
		textArea.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (state == State.EDIT)
					setNormalState();
			}
		});
		textArea.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				updateHtml();
				if (state == State.EDIT)
					setNormalState();
			}
		});
		textArea.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						adjustSize();
					}
				});
			}
		});

		initWidget(panel);

		setValue(null);
		setMode(Mode.NORMAL);
	}

	private void setNormalState() {
		if (mode == Mode.INPUT)
			return;

		panel.setWidget(html);
		html.removeStyleName(css.htmlHighlight());
		state = State.NORMAL;
	}

	private void setHighlightState() {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(html);
		html.addStyleName(css.htmlHighlight());
		state = State.HOVER;
	}

	private void setEditState(boolean focus) {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(textArea);
		html.removeStyleName(css.htmlHighlight());
		state = State.EDIT;

		if (focus) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					textArea.setFocus(true);
				}
			});
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			final ValueChangeHandler<String> handler) {
		return textArea.addValueChangeHandler(new ValueChangeHandler<String>() {
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
		return textArea.getValue();
	}

	@Override
	public void setValue(String value) {
		setValue(value, false);
	}

	@Override
	public void setValue(String value, boolean fireEvents) {
		textArea.setValue(value, fireEvents);
		if (!fireEvents)
			updateHtml();
		adjustSize();
	}

	private void updateHtml() {
		String value = getValue();
		if (value != null) {
			html.setHTML(new SafeHtmlBuilder().appendEscapedLines(value).toSafeHtml());
			html.removeStyleName(css.htmlEmpty());
		}
		else {
			if (emptyText != null) {
				html.setHTML(new SafeHtmlBuilder().appendEscapedLines(emptyText)
						.toSafeHtml());
				html.addStyleName(css.htmlEmpty());
			}
			else
				html.setHTML("&nbsp;");
		}
	}

	@Override
	public void setWidth(String width) {
		super.setWidth(width);

		html.setWidth(width);
		textArea.setWidth(width);
	}

	@Override
	public void setHeight(String height) {
		super.setHeight(height);

		html.setHeight(height);
		textArea.setHeight(height);
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

	public void setMinWidthPx(int minWidthPx) {
		this.minWidthPx = minWidthPx;
	}

	public void setMaxWidthPx(Integer maxWidthPx) {
		this.maxWidthPx = maxWidthPx;
	}

	public void setEmptyWidthPx(int emptyWidthPx) {
		this.emptyWidthPx = emptyWidthPx;
	}

	public void setEmptyHeightPx(int emptyHeightPx) {
		this.emptyHeightPx = emptyHeightPx;
	}

	private void adjustSize() {
		String value = textArea.getValue();

		int widthPx = emptyWidthPx;
		int heightPx = emptyHeightPx;
		if (!Strings.isNullOrEmpty(value)) {
			DivElement clearDiv = Document.get().createDivElement();
			clearDiv.getStyle().setProperty("clear", "both");
			Document.get().getBody().appendChild(clearDiv);

			SpanElement widthSpan = Document.get().createSpanElement();
			widthSpan.setInnerHTML(new SafeHtmlBuilder()
					.appendEscapedLines(value + "M").toSafeHtml().asString());
			widthSpan.getStyle().setVisibility(Visibility.HIDDEN);
			Document.get().getBody().appendChild(widthSpan);
			widthPx = widthSpan.getOffsetWidth() + 10;
			heightPx = widthSpan.getOffsetHeight() + 2;

			clearDiv.removeFromParent();
			widthSpan.removeFromParent();

			if (widthPx < minWidthPx)
				widthPx = minWidthPx;
			if (maxWidthPx != null && widthPx > maxWidthPx)
				widthPx = maxWidthPx;
		}

		html.setSize(widthPx + "px", heightPx + "px");
		textArea.setSize(widthPx + "px", heightPx + "px");
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

	public void setInvalid(boolean invalid) {
		html.setStyleName(Resources.INSTANCE.textAreaCss().htmlInvalid(), invalid);
	}

	/** Enables validation. If value is invalid a field is marked as invalid (with
	 * a red border)
	 * and {@link ValueChangeEvent} is not fired. */
	public void setValidator(Validator<String> validator) {
		this.validator = validator;
	}
}
