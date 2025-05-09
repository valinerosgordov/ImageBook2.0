package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Document;
import ru.minogin.core.client.flow.BaseMessage;

public class DocumentSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 3856918305096787563L;

	public static final String DOCUMENT = "document";

	public DocumentSelectedMessage(Document document) {
		super(SiteMessages.DOCUMENT_SELECTED);

		set(DOCUMENT, document);
	}

	public Document getDocument() {
		return get(DOCUMENT);
	}
}
