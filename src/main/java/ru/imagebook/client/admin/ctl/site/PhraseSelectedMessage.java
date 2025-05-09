package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Phrase;
import ru.minogin.core.client.flow.BaseMessage;

public class PhraseSelectedMessage extends BaseMessage {
	private static final long serialVersionUID = 5677794194226082518L;

	public static final String PHRASE = "phrase";

	public PhraseSelectedMessage(Phrase phrase) {
		super(SiteMessages.PHRASE_SELECTED);

		set(PHRASE, phrase);
	}

	public Phrase getPhrase() {
		return get(PHRASE);
	}
}
