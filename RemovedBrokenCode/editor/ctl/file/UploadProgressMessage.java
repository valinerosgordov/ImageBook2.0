package ru.imagebook.client.editor.ctl.file;

import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class UploadProgressMessage extends BaseMessage {
	private static final long serialVersionUID = -2342589585256202282L;

	public static final String UPLOADED = "uploaded";
	public static final String TOTAL = "total";

	UploadProgressMessage() {}

	public UploadProgressMessage(long uploaded, long total) {
		super(FileMessages.UPLOAD_PROGRESS);

		addAspects(RemotingAspect.CLIENT);

		set(UPLOADED, uploaded);
		set(TOTAL, total);
	}

	public long getUploaded() {
		return (Long) get(UPLOADED);
	}

	public long getTotal() {
		return (Long) get(TOTAL);
	}
}
