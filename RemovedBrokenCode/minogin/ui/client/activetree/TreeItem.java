package ru.minogin.ui.client.activetree;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwtquery.plugins.draggable.client.gwt.DraggableWidget;

public abstract class TreeItem<T> extends DraggableWidget<Widget> {
	private final T value;

	private HorizontalPanel panel;
	private State state = State.NORMAL;
	
	static int i = 0;

	public TreeItem(final T value, TreeRenderer<T> renderer) {
		this.value = value;

		panel = new HorizontalPanel();
		panel.getElement().setId("hPanel"  + i++);
		panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		panel.addStyleName(Resources.INSTANCE.css().itemPanel());

		Widget widget = renderer.render(value);
		panel.add(widget);

		panel.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				onOver(event);

				if (state == State.NORMAL) {
					panel.addStyleName(Resources.INSTANCE.css().itemPanelHover());

					state = State.HOVER;
				}
			}
		}, MouseOverEvent.getType());
		panel.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				onOut(TreeItem.this.getWidget(), event);

				if (state == State.HOVER) {
					panel.removeStyleName(Resources.INSTANCE.css().itemPanelHover());
					state = State.NORMAL;
				}
			}
		}, MouseOutEvent.getType());
		panel.addDomHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onSelect();
			}
		}, ClickEvent.getType());

		initWidget(panel);
	}

	protected abstract void onOver(MouseOverEvent sourceEvent);

	protected abstract void onOut(Widget widget, MouseOutEvent sourceEvent);

	protected abstract void onSelect();

	public T getValue() {
		return value;
	}

	public HorizontalPanel getPanel() {
		return panel;
	}

	public void select() {
		panel.addStyleName(Resources.INSTANCE.css().itemPanelSelected());
	}

	public void deselect() {
		panel.removeStyleName(Resources.INSTANCE.css().itemPanelSelected());
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
