package ru.minogin.ui.client.list;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class ListItemTools<T> {
	public interface ListItemToolsRenderer<T> {
		List<? extends Widget> render(T value);
	}

	private FlowPanel panel;
	private boolean panelOver;
	private boolean listOver;
	private List<? extends Widget> tools;

	public ListItemTools(final ActiveList<T> list,
			final ListItemToolsRenderer<T> renderer) {
		panel = new FlowPanel();

		Resources.INSTANCE.listItemToolsCss().ensureInjected();
		panel.addStyleName(Resources.INSTANCE.listItemToolsCss().panel());

		final Style style = panel.getElement().getStyle();
		style.setPosition(Position.ABSOLUTE);

		panel.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				panelOver = true;
				style.setOpacity(1);
			}
		}, MouseOverEvent.getType());

		panel.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				panelOver = false;
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (!listOver)
							panel.setVisible(false);
					}
				});
			}
		}, MouseOutEvent.getType());

		panel.setVisible(false);
		RootPanel.get().add(panel);

		list.addMouseOverItemHandler(new MouseOverItemHandler<T>() {
			@Override
			public void onMouseOverItem(MouseOverItemEvent<T> event) {
				if (list.isDragging()) {
					if (panel.isVisible())
						panel.setVisible(false);
					return;
				}

				listOver = true;

				removeTools();
				tools = renderer.render(event.getValue());
				if (tools != null) {
					for (Widget tool : tools) {
						panel.add(tool);
					}
				}

				ListItem<T> item = event.getItem();
				int itemLeft = item.getAbsoluteLeft();
				int itemTop = item.getAbsoluteTop();
				int itemWidth = item.getOffsetWidth();
				int itemHeight = item.getOffsetHeight();

				panel.setVisible(true);
				int panelWidth = panel.getOffsetWidth();
				int panelHeight = panel.getOffsetHeight();

				int panelLeft = itemLeft + itemWidth - 1;
				int panelTop = itemTop + (itemHeight - panelHeight) / 2;

				if (panelLeft + panelWidth > Window.getScrollLeft() + Window.getClientWidth()) {
					panelLeft = Window.getScrollLeft() + Window.getClientWidth() - panelWidth;
					panelTop = itemTop + itemHeight - 1;
				}

				panel.getElement().getStyle().setLeft(panelLeft, Unit.PX);
				panel.getElement().getStyle().setTop(panelTop, Unit.PX);
			}
		});

		list.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
// panel.setVisible(false);
// removeTools();
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (!panelOver) {
							panel.setVisible(false);
							removeTools();
						}
					}
				});
				listOver = false;
			}
		}, MouseOutEvent.getType());

		list.addClearHandler(new ClearHandler() {
			@Override
			public void onClear(ClearEvent event) {
				removeTools();
			}
		});

		list.addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (!event.isAttached())
					panel.removeFromParent();
			}
		});
	}

	private void removeTools() {
		if (tools != null) {
			for (Widget tool : tools) {
				tool.removeFromParent();
			}
		}
	}
}
