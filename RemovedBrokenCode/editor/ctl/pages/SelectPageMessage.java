package ru.imagebook.client.editor.ctl.pages;

import org.aspectj.weaver.ast.Var;
import ru.minogin.core.client.flow.BaseMessage;

public class SelectPageMessage extends BaseMessage {
	private static final long serialVersionUID = -1213285913834714236L;

	public static final String PAGE_NUMBER = "pageNumber";
	public static final String VARIANT = "variant";

	public SelectPageMessage(int pageNumber, int variant) {
		super(PagesMessages.SELECT_PAGE);

		set(PAGE_NUMBER, pageNumber);
		set(VARIANT, variant);
	}

	public int getPageNumber() {
		return (Integer) get(PAGE_NUMBER);
	}
	public int getVariant() { return (Integer) get(VARIANT); }
}
