package ru.minogin.ui.client.list;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** An active list for displaying, selecting and reordering list items.
 * Every item is rendered as a row of widgets.
 * <br/><br/>
 * Values are provided by {@link #setValues(Collection)}.
 * <br/><br/>
 * You may add tools (e.g. buttons) for the list items using
 * {@link ListItemTools} and the tools for the whole list using
 * {@link ListTools}.
 * <br/><br/>
 * Items reordering may be enabled with {@link #setOrdering(boolean)}.
 * 
 * @author Andrey Minogin
 * 
 * @param <T> */
public class ActiveList<T> extends Composite implements HasMouseOverHandlers,
        HasMouseOutHandlers {
	private final ActiveListRenderer<T> renderer;
	private ActiveListHeaderRenderer headerRenderer;

	private Map<T, ListItem<T>> items = new HashMap<T, ListItem<T>>();
	private ListItem<T> selectedItem;

	private FlowPanel itemsPanel;
	private boolean dragging;
	private String emptyText;

	private boolean ordering = false;
	private int level = 0;

	public ActiveList(ActiveListRenderer<T> renderer) {
		this.renderer = renderer;

		Resources.INSTANCE.css().ensureInjected();

		itemsPanel = new FlowPanel();
		itemsPanel.addStyleName(Resources.INSTANCE.css().itemsPanel());

		initWidget(itemsPanel);
	}

	/** Enables rendering of header row with column labels. */
	public void setHeaderRenderer(ActiveListHeaderRenderer headerRenderer) {
		this.headerRenderer = headerRenderer;
	}

	@Deprecated
	public void setValues(Collection<T> values) {
		showValues(values);
	}

	public void showValues(Collection<T> values) {
		int scrollLeft = Window.getScrollLeft();
		int scrollTop = Window.getScrollTop();

		clear();

		renderHeader();

		for (final T value : values) {
			addItem(value);
		}

		if (values.isEmpty() && emptyText != null) {
			Label label = new Label(emptyText);
			label.addStyleName(Resources.INSTANCE.css().emptyText());
			itemsPanel.add(label);
		}

		Window.scrollTo(scrollLeft, scrollTop);

		fireEvent(new RenderItemsEvent());
	}

	private void renderHeader() {
		if (headerRenderer == null)
			return;

		List<? extends Widget> widgets = headerRenderer.render();
		if (widgets == null)
			return;

		FlowPanel headerPanel = new FlowPanel();
		headerPanel.addStyleName(Resources.INSTANCE.css().headerPanel());
		for (Widget widget : widgets) {
			headerPanel.add(widget);
		}
		itemsPanel.add(headerPanel);
	}

	public void addValues(Collection<T> values) {
		for (final T value : values) {
			addItem(value);
		}
	}

	public void updateValue(T value) {
		ListItem<T> item = items.get(value);
		if (item == null)
			return;

		item.update(value);
	}

	public void removeValue(T value) {
		ListItem<T> item = items.remove(value);
		if (item == null)
			return;

		itemsPanel.remove(item);
	}

	private void addItem(final T value) {
		final ListItem<T> item = new ListItem<T>(value, renderer);
		item.setLevel(level);

		item.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				setSelected(item);
			}
		});

		item.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				ActiveList.this
						.fireEvent(new MouseOverItemEvent<T>(value, item, event));
			}
		});

		item.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				ActiveList.this.fireEvent(new MouseOutItemEvent<T>(value, item, event));
			}
		});

		if (ordering) {
			item.makeDraggable();
			item.addDragStartHandler(new DragStartHandler<T>() {
				@Override
				public void onDragStart(DragStartEvent<T> event) {
					dragging = true;
				}
			});
			item.addDragEndHandler(new DragEndHandler<T>() {
				@Override
				public void onDragEnd(DragEndEvent<T> event) {
					dragging = false;

					ListItem<T> target = getTarget(item);
					if (target != null) {
						int targetIndex = itemsPanel.getWidgetIndex(target);
						fireEvent(new MoveEvent<T>(item.getValue(), targetIndex));
					}
					else {
						item.cancelDrag();
					}
				}
			});
			item.addDragHandler(new DragHandler<T>() {
				@SuppressWarnings("unchecked")
				@Override
				public void onDrag(DragEvent<T> event) {
					for (int i = 0; i < itemsPanel.getWidgetCount(); i++) {
						ListItem<T> item = (ListItem<T>) itemsPanel.getWidget(i);
						item.removeStyleName(Resources.INSTANCE.css().mayDropTop());
						item.removeStyleName(Resources.INSTANCE.css().mayDropBottom());
					}

					ListItem<T> target = getTarget(item);
					if (target != null) {
						int index = itemsPanel.getWidgetIndex(item);
						int targetIndex = itemsPanel.getWidgetIndex(target);

						if (targetIndex < index)
							target.addStyleName(Resources.INSTANCE.css().mayDropTop());
						else if (targetIndex > index)
							target.addStyleName(Resources.INSTANCE.css().mayDropBottom());
					}
				}
			});
		}

		itemsPanel.add(item);
		items.put(value, item);
	}

	private void clear() {
		fireEvent(new ClearEvent());

		itemsPanel.clear();
		items.clear();
	}

	@SuppressWarnings("unchecked")
	private ListItem<T> getTarget(ListItem<T> draggable) {
		int dX1 = draggable.getAbsoluteLeft();
		int dX2 = dX1 + draggable.getOffsetWidth();
		int top = draggable.getAbsoluteTop();
		int index = itemsPanel.getWidgetIndex(draggable);

		for (int i = 0; i < index; i++) {
			ListItem<T> item = (ListItem<T>) itemsPanel.getWidget(i);
			int itemX1 = item.getAbsoluteLeft();
			int itemX2 = itemX1 + item.getOffsetWidth();
			if (dX1 <= itemX2 && dX2 >= itemX1 && item.getAbsoluteTop() > top)
				return item;
		}
		for (int i = itemsPanel.getWidgetCount() - 1; i > index; i--) {
			ListItem<T> item = (ListItem<T>) itemsPanel.getWidget(i);
			int itemX1 = item.getAbsoluteLeft();
			int itemX2 = itemX1 + item.getOffsetWidth();
			if (dX1 <= itemX2 && dX2 >= itemX1 && item.getAbsoluteTop() < top)
				return item;
		}
		return null;
	}

	public HandlerRegistration addMouseOverItemHandler(
			MouseOverItemHandler<T> handler) {
		return addHandler(handler, MouseOverItemEvent.TYPE);
	}

	public HandlerRegistration addMouseOutItemHandler(
			MouseOutItemHandler<T> handler) {
		return addHandler(handler, MouseOutItemEvent.TYPE);
	}

	public HandlerRegistration addMoveHandler(MoveHandler<T> handler) {
		return addHandler(handler, MoveEvent.TYPE);
	}

	public int getIndex(ListItem<T> item) {
		return itemsPanel.getWidgetIndex(item);
	}

	public boolean isDragging() {
		return dragging;
	}

	public void setEmptyText(String emptyText) {
		this.emptyText = emptyText;
	}

	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return addDomHandler(handler, MouseOverEvent.getType());
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return addDomHandler(handler, MouseOutEvent.getType());
	}

	/** Enable or disable items ordering (drag and drop).<br/>
	 * Must be called before setValues(...) */
	public void setOrdering(boolean ordering) {
		this.ordering = ordering;
	}

	public HandlerRegistration addSelectHandler(ActiveListSelectHandler<T> handler) {
		return addHandler(handler, ActiveListSelectEvent.TYPE);
	}

	public void setSelected(T value) {
		ListItem<T> item = items.get(value);
		setSelected(item);
	}

	private void setSelected(ListItem<T> item) {
		if (selectedItem != null && selectedItem != item)
			selectedItem.deselect();

		if (item != null && item != selectedItem)
			item.select();

		selectedItem = item;

		if (item != null)
			ActiveList.this.fireEvent(new ActiveListSelectEvent<T>(item.getValue()));
	}

	public HandlerRegistration addClearHandler(ClearHandler handler) {
		return addHandler(handler, ClearEvent.TYPE);
	}

	public HandlerRegistration addRenderItemsHandler(RenderItemsHandler handler) {
		return addHandler(handler, RenderItemsEvent.TYPE);
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
