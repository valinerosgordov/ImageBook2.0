package ru.imagebook.client.admin.ctl.site;

import ru.imagebook.shared.model.site.Phrase;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.remoting.RemotingAspect;
import ru.saasengine.client.ctl.auth.SessionAspect;

public class SavePhraseMessage extends BaseMessage {
	private static final long serialVersionUID = 2363050691935529523L;
	
	public static final String PHRASE = "phrase";

	SavePhraseMessage() {}

	public SavePhraseMessage(Phrase phrase) {
		super(SiteMessages.SAVE_PHRASE);

		addAspects(RemotingAspect.REMOTE, SessionAspect.SESSION);

		set(PHRASE, phrase);
	}

	public Phrase getPhrase() {
		return get(PHRASE);
	}
}
