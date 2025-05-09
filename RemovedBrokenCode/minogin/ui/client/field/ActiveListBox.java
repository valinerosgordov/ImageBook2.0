package ru.minogin.ui.client.field;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/** An active field similar to {@link ActiveTextBox} for displaying and editing
 * list values. Values are rendered by {@link ActiveListBoxRenderer}.
 * Available values are specified via {@link #setValues(List)} <br/><br/>
 * The list also supports null value, but this may be disabled with
 * {@link #setNullable(boolean)} <br/><br/>
 * You should listen to value changes via
 * {@link #addValueChangeHandler(ValueChangeHandler)} and apply changes
 * immediately.<br/><br/>
 * 
 * @author Andrey Minogin
 * 
 * @param <T>
 * 
 * @see ActiveTextBox */
public class ActiveListBox<T> extends Composite implements HasValue<T>,
        Focusable {
	public interface ActiveListBoxRenderer<T> {
		String render(T value);
	}

	private State state = State.NORMAL;
	private FocusPanel panel;
	private HTML html;
	private ListBox listBox;
	private String emptyText;
	private List<T> values = new ArrayList<T>();
	private boolean nullable = true;
	private final ActiveListBoxRenderer<T> renderer;
	private Validator<T> validator;
	private Mode mode;

	public ActiveListBox(ActiveListBoxRenderer<T> renderer, T value, String emptyText,
			ValueChangeHandler<T> handler) {
		this(renderer);

		setValue(value);
		setEmptyText(emptyText);
		addValueChangeHandler(handler);
	}

	public ActiveListBox(ActiveListBoxRenderer<T> renderer) {
		this.renderer = renderer;

		Resources.INSTANCE.listBoxCss().ensureInjected();

		panel = new FocusPanel();
		panel.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				setEditState(true);
			}
		});

		html = new HTML();
		html.addStyleName(Resources.INSTANCE.listBoxCss().html());
		html.addStyleName(Resources.INSTANCE.listBoxCss().htmlEmpty());
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

		listBox = new ListBox();
		listBox.addStyleName(Resources.INSTANCE.listBoxCss().listBox());
		listBox.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (state == State.EDIT)
					setNormalState();
			}
		});
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				updateHtml();
				if (state == State.EDIT)
					setNormalState();
				ValueChangeEvent.fire(ActiveListBox.this, getValue());
			}
		});

		initWidget(panel);

		setValue(null);
		// setWidth("15em");
		setMode(Mode.NORMAL);
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public void setValues(List<T> values) {
		this.values.clear();
		if (nullable)
			this.values.add(null);
		this.values.addAll(values);
		renderValues();
	}

	private void renderValues() {
		listBox.clear();
		for (T value : values) {
			String item = null;
			if (value != null)
				item = renderer.render(value);
			else
				item = "-";
			listBox.addItem(item);
		}
	}

	private void setNormalState() {
		if (mode == Mode.INPUT)
			return;

		panel.setWidget(html);
		html.removeStyleName(Resources.INSTANCE.listBoxCss().htmlHover());
		state = State.NORMAL;
	}

	private void setHoverState() {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(html);
		html.addStyleName(Resources.INSTANCE.listBoxCss().htmlHover());
		state = State.HOVER;
	}

	private void setEditState(boolean focus) {
		if (mode == Mode.READONLY)
			return;

		panel.setWidget(listBox);
		html.removeStyleName(Resources.INSTANCE.listBoxCss().htmlHover());
		state = State.EDIT;

		if (focus) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand() {
				@Override
				public void execute() {
					listBox.setFocus(true);
				}
			});
		}
	}

	@Override
	public HandlerRegistration addValueChangeHandler(
			final ValueChangeHandler<T> handler) {
		return addHandler(new ValueChangeHandler<T>() {
			@Override
			public void onValueChange(ValueChangeEvent<T> event) {
				if (validator == null) {
					handler.onValueChange(event);
					return;
				}

				T value = getValue();
				if (validator.validate(value)) {
					setInvalid(false);
					handler.onValueChange(event);
				}
				else {
					setInvalid(true);
				}
			}
		}, ValueChangeEvent.getType());
	}

	@Override
	public T getValue() {
		int index = listBox.getSelectedIndex();

		if (nullable && index == 0)
			return null;

		if (index != -1)
			return values.get(index);
		else
			return null;
	}

	@Override
	public void setValue(T value) {
		setValue(value, false);
	}

	@Override
	public void setValue(T value, boolean fireEvents) {
		int index = values.indexOf(value);
		listBox.setSelectedIndex(index);

		if (!fireEvents)
			updateHtml();
	}

	private void updateHtml() {
		T value = getValue();
		if (value != null) {
			html.setText(renderer.render(value));
			html.removeStyleName(Resources.INSTANCE.listBoxCss().htmlEmpty());
		}
		else {
			if (emptyText != null) {
				html.setText(emptyText);
				html.addStyleName(Resources.INSTANCE.listBoxCss().htmlEmpty());
			}
			else
				html.setHTML("&nbsp;");
		}
	}

	// TODO
	@Override
	public void setWidth(String width) {
		// super.setWidth(width);
		//
		// html.setWidth(width);
		// listBox.setWidth(width);
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

	@SuppressWarnings("deprecation")
	public void setMultipleSelect(boolean multiple) {
		listBox.setMultipleSelect(multiple);
	}

	public void setInvalid(boolean invalid) {
		html.setStyleName(Resources.INSTANCE.listBoxCss().htmlInvalid(), invalid);
	}

	/** Enables validation. If value is invalid a field is marked as invalid (with
	 * a red border)
	 * and {@link ValueChangeEvent} is not fired. */
	public void setValidator(Validator<T> validator) {
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
