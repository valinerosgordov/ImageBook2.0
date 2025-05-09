package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class ResizingProgressMessage extends BaseMessage {
	private static final long serialVersionUID = -3029526602188096674L;

	public static final String RESIZED = "resized";
	public static final String TOTAL = "total";

	ResizingProgressMessage() {}

	public ResizingProgressMessage(int resized, int total) {
		super(FileMessages.RESIZING_PROGRESS);

		addAspects(RemotingAspect.CLIENT);

		set(RESIZED, resized);
		set(TOTAL, total);
	}

	public int getResized() {
		return (Integer) get(RESIZED);
	}

	public int getTotal() {
		return (Integer) get(TOTAL);
	}
}
