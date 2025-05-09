package ru.minogin.ui.client.activetree;


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

/**
 * Adds tools (e.g. buttons) to the every item of the tree.
 * 
 * @author Andrey Minogin
 *
 * @param <T> - tree item type.
 */
public class TreeTools<T> {
	private final FlowPanel panel;
	private boolean panelOver;
	private boolean treeOver;
	private T value;

	public TreeTools(final ActiveTree<T> tree) {
		panel = new FlowPanel();

		final Style style = panel.getElement().getStyle();
		style.setPosition(Position.ABSOLUTE);
		style.setBackgroundColor("#eee");
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
						if (!treeOver)
							panel.setVisible(false);
					}
				});
			}
		}, MouseOutEvent.getType());
		RootPanel.get().add(panel);
		panel.setVisible(false);

		tree.addItemMouseOverHandler(new ItemMouseOverHandler<T>() {
			@Override
			public void onItemMouseOver(ItemMouseOverEvent<T> event) {
				if (tree.isDragging()) {
					if (panel.isVisible())
						panel.setVisible(false);
					return;
				}

				treeOver = true;

				panel.setVisible(true);
				TreeItem<T> item = event.getItem();

				value = event.getValue();

				int panelLeft = item.getAbsoluteLeft() + item.getOffsetWidth();
				int itemTop = item.getAbsoluteTop();
				int panelTop = itemTop + 1; // TODO
				panel.getElement().getStyle().setLeft(panelLeft, Unit.PX);
				panel.getElement().getStyle().setTop(panelTop, Unit.PX);
				panel.getElement().getStyle()
						.setHeight(item.getOffsetHeight(), Unit.PX);
			}
		});
		tree.addDomHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				treeOver = false;
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {
					@Override
					public void execute() {
						if (!panelOver)
							panel.setVisible(false);
					}
				});
			}
		}, MouseOutEvent.getType());

		tree.addAttachHandler(new Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (!event.isAttached())
					panel.removeFromParent();
			}
		});
	}

	public void addTool(Widget tool) {
		tool.getElement().getStyle().setMarginLeft(0.5, Unit.EM);  // TODO
		panel.add(tool);
	}

	/**
	 * @return Current tool item.
	 */
	public T getValue() {
		return value;
	}
}
