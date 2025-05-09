package ru.minogin.core.client.tip;

import java.util.List;

import ru.minogin.core.client.collections.XArrayList;

public class Tip {
	public static final String TOP_LEFT = "topLeft";
	public static final String TOP_MIDDLE = "topMiddle";
	public static final String TOP_RIGHT = "topRight";
	public static final String RIGHT_TOP = "rightTop";
	public static final String RIGHT_MIDDLE = "rightMiddle";
	public static final String RIGHT_BOTTOM = "rightBottom";
	public static final String BOTTOM_RIGHT = "bottomRight";
	public static final String BOTTOM_MIDDLE = "bottomMiddle";
	public static final String BOTTOM_LEFT = "bottomLeft";
	public static final String LEFT_BOTTOM = "leftBottom";
	public static final String LEFT_MIDDLE = "leftMiddle";
	public static final String LEFT_TOP = "leftTop";
	private static final List<String> pos = new XArrayList<String>(TOP_LEFT, TOP_MIDDLE, TOP_RIGHT,
			RIGHT_TOP, RIGHT_MIDDLE, RIGHT_BOTTOM, BOTTOM_RIGHT, BOTTOM_MIDDLE, BOTTOM_LEFT, LEFT_BOTTOM,
			LEFT_MIDDLE, LEFT_TOP);

	private String targetId;
	private String title;
	private String text;
	private String style = "default";
	private String hookTarget;
	private String hookTip;
	private String stem = "";
	private int dx, dy;
	private int width;
	private boolean closeable;
	private Object elem;

	public Tip(String targetId, String text) {
		this.targetId = targetId;
		this.text = text;
	}

	public String getTargetId() {
		return targetId;
	}

	public String getText() {
		return text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setHook(String target, String tip) {
		hookTarget = target;
		hookTip = tip;
	}

	public String getHookTarget() {
		return hookTarget;
	}

	public String getHookTip() {
		return hookTip;
	}

	public String getStem() {
		return stem;
	}

	public void setStem(String stem) {
		this.stem = stem;
	}

	public void setStemAndHook(String stem) {
		setStem(stem);
		setHook(opp(stem), stem);
	}

	public void setOffset(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isCloseable() {
		return closeable;
	}

	public void setCloseable(boolean closeable) {
		this.closeable = closeable;
	}

	public void show() {
		doShow();
	}

	private native void doShow()
	/*-{
		var targetId = this.@ru.minogin.core.client.tip.Tip::targetId;
		var title = this.@ru.minogin.core.client.tip.Tip::title;
		var text = this.@ru.minogin.core.client.tip.Tip::text;
		var style = this.@ru.minogin.core.client.tip.Tip::style;
		var hookTarget = this.@ru.minogin.core.client.tip.Tip::hookTarget;
		var hookTip = this.@ru.minogin.core.client.tip.Tip::hookTip;
		var stem = this.@ru.minogin.core.client.tip.Tip::stem;
		var dx = this.@ru.minogin.core.client.tip.Tip::dx;
		var dy = this.@ru.minogin.core.client.tip.Tip::dy;
		var width = this.@ru.minogin.core.client.tip.Tip::width;
		var closeable = this.@ru.minogin.core.client.tip.Tip::closeable;
		if (width == 0)
			width = false;

		new $wnd.Tip(targetId, text, {
			title: title,
			hook: { target: hookTarget, tip: hookTip },
			style: style,
			stem: stem,
			hideOn: false,
			offset: { x: dx, y: dy },
			width: width,
			closeButton: closeable
		});

		elem = $wnd.document.getElementById(targetId);
		this.@ru.minogin.core.client.tip.Tip::elem = elem;
		elem.prototip.show();
	}-*/;

	public void hide() {
		doHide();
	}

	private native void doHide()
	/*-{
		var targetId = this.@ru.minogin.core.client.tip.Tip::targetId;
		elem = $wnd.document.getElementById(targetId);
		if (elem != null && elem.prototip != null)
			elem.prototip.hide();
	}-*/;

	public void remove() {
		doRemove();
	}

	private native void doRemove()
	/*-{
		elem = this.@ru.minogin.core.client.tip.Tip::elem;
		if (elem != null && elem.prototip != null)
			elem.prototip.remove();
	}-*/;

	public static String opp(String position) {
		int i = pos.indexOf(position);
		i = (i + 6) % pos.size();
		return pos.get(i);
	}
}
