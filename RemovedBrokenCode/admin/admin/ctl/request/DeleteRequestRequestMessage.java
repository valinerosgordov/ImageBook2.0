package ru.imagebook.client.admin.ctl.request;

import java.util.List;

import ru.imagebook.shared.model.Request;
import ru.minogin.core.client.flow.BaseMessage;

public class DeleteRequestRequestMessage extends BaseMessage {
	private static final long serialVersionUID = -5749922644213462857L;

	public static final String REQUESTS = "requests";

	public DeleteRequestRequestMessage(List<Request> requests) {
		super(RequestMessages.DELETE_REQUEST);

		set(REQUESTS, requests);
	}

	public List<Request> getRequests() {
		return get(REQUESTS);
	}
}
