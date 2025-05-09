package ru.imagebook.client.editor.ctl.file;

import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class DisposeResultMessage extends BaseMessage {
	private static final long serialVersionUID = -7420130994077225480L;
	
	public static final String ORDER = "order";

	DisposeResultMessage() {}

	public DisposeResultMessage(Order<?> order) {
		super(FileMessages.DISPOSE_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(ORDER, order);
	}

	public Order<?> getOrder() {
		return get(ORDER);
	}
}
