package ru.minogin.core.client.gxt;

import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.core.XDOM;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentPlugin;

public class HintPlugin implements ComponentPlugin {
	public static final String HINT = "hint";

	private String hint;

	public HintPlugin(String hint) {
		this.hint = hint;
	}

	@Override
	public void init(Component component) {
		component.addListener(Events.Render, new Listener<ComponentEvent>() {
			public void handleEvent(ComponentEvent be) {
				El elem = be.getComponent().el().findParent(".x-form-element", 3);
				elem.appendChild(XDOM.create("<div class='hint-plugin'>" + hint + "</div>"));
			}
		});
	}
}
