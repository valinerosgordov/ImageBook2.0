package ru.minogin.gwt.client.rpc;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.Duration;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/** @author Andrey Minogin
 * 
 * <p>Usage:</p>
 * 
 * <pre>
 * FailurePanel panel = new FailurePanel();
 * panel.setHTML("My error message");
 * panel.show();
 * </pre> */
public class FailurePanel extends Composite {
	public static final int WIDTH = 300;
	public static final int MARGIN_TOP = 50;
	public static final int MARGIN_RIGHT = 100;

	public static final int SHOW_TIME_SEC = 10;
	public static final int BLUR_TIME_SEC = 5;

	private String html;

	public FailurePanel() {

	}

	public void setHTML(String html) {
		this.html = html;
	}

	@Override
	protected void onAttach() {
		super.onAttach();

		new Animation() {
			@Override
			protected void onUpdate(double progress) {
				Style style = getElement().getStyle();
				style.setOpacity(1 - progress);
			}

			@Override
			protected void onComplete() {
				super.onComplete();

				removeFromParent();
			}
		}.run(BLUR_TIME_SEC * 1000, Duration.currentTimeMillis() + SHOW_TIME_SEC
				* 1000);
	}

	public void show() {
		Resources.INSTANCE.css().ensureInjected();

		SimplePanel panel = new SimplePanel();
		panel.addStyleName(Resources.INSTANCE.css().failurePanel());
		panel.add(new HTML(html));
		initWidget(panel);

		Style style = getElement().getStyle();
		style.setLeft(Window.getClientWidth() - WIDTH - MARGIN_RIGHT, Unit.PX);
		style.setTop(MARGIN_TOP, Unit.PX);

		RootPanel.get().add(this);
	}
}
