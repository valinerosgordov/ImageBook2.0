package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.RootPanel;

public class Desktop extends Viewport {
	private ToolBar toolBar;
	private Workspace workspace;

	public Desktop(String title) {
		setLayout(new RowLayout());

		Text text = new Text(title);
		text.setId("app-title");
		add(text, new RowData(1, -1));

		toolBar = new ToolBar();
		add(toolBar, new RowData(1, -1));

		workspace = new Workspace();

		add(workspace, new RowData(1, 1));
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public Workspace dispose() {
		RootPanel.get().add(this);
		return workspace;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}
}
