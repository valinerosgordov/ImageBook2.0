package ru.imagebook.client.editor.ctl.pages;

import ru.imagebook.shared.model.editor.Layout;
import ru.minogin.core.client.flow.BaseMessage;

public class SelectLayoutMessage extends BaseMessage {
	private static final long serialVersionUID = 1L;

	public static final String VARIANT = "variant";
	public static final String LAYOUT = "layout";

	public SelectLayoutMessage(int variant, Layout layout) {
		super(PagesMessages.SELECT_LAYOUT);
		set(VARIANT, variant);
		set(LAYOUT, layout);
	}

	public int getVariant() { return (Integer) get(VARIANT); }
	public Layout getlayout() { return (Layout) get(LAYOUT); }
}
