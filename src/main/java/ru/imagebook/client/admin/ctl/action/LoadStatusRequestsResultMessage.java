package ru.imagebook.client.admin.ctl.action;

import java.util.List;

import ru.imagebook.shared.model.StatusRequest;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadStatusRequestsResultMessage extends BaseMessage {
	private static final long serialVersionUID = 615928157855233079L;
	public static final String CODES = "codes";

	LoadStatusRequestsResultMessage() {}

	public LoadStatusRequestsResultMessage(List<StatusRequest> codes) {
		super(ActionMessages.LOAD_STATUS_REQUESTS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(CODES, codes);
	}

	public List<StatusRequest> getCodes() {
		return get(CODES);
	}
}
