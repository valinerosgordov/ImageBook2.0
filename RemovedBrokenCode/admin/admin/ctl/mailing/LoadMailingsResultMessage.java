package ru.imagebook.client.admin.ctl.mailing;

import java.util.List;

import ru.imagebook.shared.model.Mailing;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadMailingsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 7180379140968557358L;

	public static final String MAILINGS = "mailings";

	LoadMailingsResultMessage() {}

	public LoadMailingsResultMessage(List<Mailing> mailings) {
		super(MailingMessages.LOAD_MAILINGS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(MAILINGS, mailings);
	}

	public List<Mailing> getMailings() {
		return get(MAILINGS);
	}
}
