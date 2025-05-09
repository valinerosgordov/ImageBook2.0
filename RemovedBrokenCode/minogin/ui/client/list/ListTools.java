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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class ListTools {
	public interface ListToolsRenderer {
		List<? extends Widget> render();
	}

	private FlowPanel panel;
	private boolean listOver;
	private boolean panelOver;
	private final ActiveList<?> list;
	private boolean emptyList = false;

	public ListTools(final ActiveList<?> list, ListToolsRenderer renderer) {
		this.list = list;
		panel = new FlowPanel();

		Resources.INSTANCE.listToolsCss().ensureInjected();
		panel.addStyleName(Resources.INSTANCE.listToolsCss().panel());

		final Style style = panel.getElement().getStyle();
		style.setPosition(Position.ABSOLUTE);

		for (Widget widget : renderer.render()) {
			panel.add(widget);
		}

		hidePanel();
		panel.addDomHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				panelOver = true;
			}
		}, MouseOverEvent.getType());
		panel.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				panelOver = false;
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (!listOver && !emptyList)
							hidePanel();
					}
				});
			}
		}, MouseOutEvent.getType());
		RootPanel.get().add(panel);

		list.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				listOver = true;

				showPanel();
			}
		});

		list.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				listOver = false;
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (!panelOver && !emptyList)
							hidePanel();
					}
				});
			}
		});

		list.addRenderItemsHandler(new RenderItemsHandler() {
			@Override
			public void onRenderItems(RenderItemsEvent event) {
				emptyList = list.isEmpty();
				if (emptyList)
					showPanel();
				else
					hidePanel();
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

	private void showPanel() {
		int panelLeft = list.getAbsoluteLeft();
		int panelTop = list.getAbsoluteTop() + list.getOffsetHeight() - 1;
		panel.getElement().getStyle().setLeft(panelLeft, Unit.PX);
		panel.getElement().getStyle().setTop(panelTop, Unit.PX);
		panel.setVisible(true);
	}

	private void hidePanel() {
		panel.setVisible(false);
	}
}
