package ru.minogin.ui.client.list;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class ListItem<T> extends FlowPanel {
	private static final int DRAG_DISTANCE = 5;

	public enum State {
		NORMAL,
		HOVER
	}

	private T value;
	private State state = State.NORMAL;
	private boolean dragging = false;
	private HandlerRegistration mouseUpHandler;
	private int relX;
	private int relY;
	private Memento memento;
	int level = 0;
	private ActiveListRenderer<T> renderer;

	public ListItem(final T value, ActiveListRenderer<T> renderer) {
		this.value = value;
		this.renderer = renderer;

		addStyleName(Resources.INSTANCE.css().listItem());

		addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (state == State.NORMAL) {
					addStyleName("itemPanelHover" + level);

					state = State.HOVER;
				}
			}
		});

		addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (state == State.HOVER) {
					removeStyleName("itemPanelHover" + level);

					state = State.NORMAL;
				}
			}
		});

		render();
	}

	public void update(T value) {
		this.value = value;

		clear();
		render();
	}

	private void render() {
		List<? extends Widget> widgets = renderer.render(value);
		if (widgets != null) {
			for (Widget widget : widgets) {
				add(widget);
			}
		}
	}

	public void setLevel(int level) {
		this.level = level;
	}

	private void startDrag(MouseMoveEvent event) {
		memento = save();

		Style style = getElement().getStyle();
		style.setPosition(Position.ABSOLUTE);
		style.setZIndex(1000);
		style.setOpacity(0.8);
		style.setCursor(Cursor.MOVE);
		fireEvent(new DragStartEvent<T>(value));
	}

	private Memento save() {
		Style style = getElement().getStyle();

		Memento memento = new Memento();
		memento.setPosition(style.getPosition());
		memento.setZIndex(style.getZIndex());
		memento.setOpacity(style.getOpacity());
		memento.setAbsoluteLeft(getAbsoluteLeft());
		memento.setAbsoluteTop(getAbsoluteTop());
		memento.setCursor(style.getCursor());
		return memento;
	}

	private void restore(Memento memento) {
		Style style = getElement().getStyle();
		style.setProperty("position", memento.getPosition());
		style.setProperty("zIndex", memento.getzIndex());
		style.setProperty("opacity", memento.getOpacity());
		style.setProperty("cursor", memento.getCursor());
		style.setLeft(memento.getAbsoluteLeft(), Unit.PX);
		style.setTop(memento.getAbsoluteTop(), Unit.PX);
	}

	private void drag(MouseMoveEvent event) {
		Style style = ListItem.this.getElement().getStyle();
		style.setLeft(event.getClientX() + Window.getScrollLeft() - relX, Unit.PX);
		style.setTop(event.getClientY() + Window.getScrollTop() - relY, Unit.PX);

		fireEvent(new DragEvent<T>(value));
	}

	public T getValue() {
		return value;
	}

	public void select() {
		addStyleName("itemPanelSelected" + level);
	}

	public void deselect() {
		removeStyleName("itemPanelSelected" + level);
	}

	public void addDragStartHandler(DragStartHandler<T> handler) {
		addHandler(handler, DragStartEvent.TYPE);
	}

	public void addDragHandler(DragHandler<T> handler) {
		addHandler(handler, DragEvent.TYPE);
	}

	public void addDragEndHandler(DragEndHandler<T> handler) {
		addHandler(handler, DragEndEvent.TYPE);
	}

	public void cancelDrag() {
		restore(memento);
	}

	public void addClickHandler(ClickHandler handler) {
		addDomHandler(handler, ClickEvent.getType());
	}

	public void addMouseOverHandler(MouseOverHandler handler) {
		addDomHandler(handler, MouseOverEvent.getType());
	}

	public void addMouseOutHandler(MouseOutHandler handler) {
		addDomHandler(handler, MouseOutEvent.getType());
	}

	public void addMouseDownHandler(MouseDownHandler handler) {
		addDomHandler(handler, MouseDownEvent.getType());
	}

	public void makeDraggable() {
		addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(final MouseDownEvent event) {
				relX = event.getX();
				relY = event.getY();

				final HandlerRegistration mouseMoveHandler = RootPanel.get()
						.addDomHandler(new MouseMoveHandler() {
							@Override
							public void onMouseMove(MouseMoveEvent event) {
								if (!dragging) {
									int dx = Math.abs(event.getRelativeX(ListItem.this
											.getElement()) - relX);
									int dy = Math.abs(event.getRelativeY(ListItem.this
											.getElement()) - relY);
									if (dx > DRAG_DISTANCE || dy > DRAG_DISTANCE) {
										startDrag(event);
										dragging = true;
									}
								}
								else
									drag(event);
							}
						}, MouseMoveEvent.getType());

				mouseUpHandler = RootPanel.get().addDomHandler(new MouseUpHandler() {
					@Override
					public void onMouseUp(final MouseUpEvent event) {
						mouseMoveHandler.removeHandler();
						mouseUpHandler.removeHandler();

						if (dragging) {
							dragging = false;
							fireEvent(new DragEndEvent<T>(value));
						}
					}
				}, MouseUpEvent.getType());
			}
		});
	}
}
