package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Document;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SaveDocumentMessage extends BaseMessage {
	private static final long serialVersionUID = 5427269919902295225L;

	public static final String DOCUMENT = "document";

	SaveDocumentMessage() {}

	public SaveDocumentMessage(Document document) {
		super(SiteMessages.SAVE_DOCUMENT);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(DOCUMENT, document);
	}

	public Document getDocument() {
		return get(DOCUMENT);
	}
}
