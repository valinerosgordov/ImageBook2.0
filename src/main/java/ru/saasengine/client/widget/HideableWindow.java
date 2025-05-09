package ru.saasengine.client.widget;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class HideableWindow extends Window {
	public static final EventType Close = new EventType();

	public HideableWindow() {
		setLayout(new FitLayout());
		setSize(600, 500);
		setMinimizable(true);
		setMaximizable(true);
		setClosable(false);
	}

	@Override
	protected void initTools() {
		super.initTools();

		ToolButton closeBtn = new ToolButton("x-tool-close");
		closeBtn.addListener(Events.Select, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent ce) {
				fireEvent(Close);
			}
		});
		head.addTool(closeBtn);
	}
}
