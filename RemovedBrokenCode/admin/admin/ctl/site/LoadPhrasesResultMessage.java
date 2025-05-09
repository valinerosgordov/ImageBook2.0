package ru.imagebook.client.admin.ctl.site;

import java.util.List;

import ru.imagebook.shared.model.site.Phrase;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadPhrasesResultMessage extends BaseMessage {
	private static final long serialVersionUID = -5978363714391160448L;

	public static final String PHRASES = "phrases";

	LoadPhrasesResultMessage() {}

	public LoadPhrasesResultMessage(List<Phrase> phrases) {
		super(SiteMessages.LOAD_PHRASES_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(PHRASES, phrases);
	}

	public List<Phrase> getPhrases() {
		return get(PHRASES);
	}
}
