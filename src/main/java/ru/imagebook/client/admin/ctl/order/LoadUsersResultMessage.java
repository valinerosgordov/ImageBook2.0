package ru.imagebook.client.admin.ctl.order;

import java.util.List;

import ru.imagebook.shared.model.User;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.hibernate.HibernateAspect;
import ru.minogin.core.client.flow.remoting.RemotingAspect;

public class LoadUsersResultMessage extends BaseMessage {
	private static final long serialVersionUID = 82838076956307846L;

	public static final String USERS = "users";
	public static final String OFFSET = "offset";
	public static final String TOTAL = "total";

	LoadUsersResultMessage() {}

	public LoadUsersResultMessage(List<User> users, int offset, long total) {
		super(OrderMessages.LOAD_USERS_RESULT);

		addAspects(RemotingAspect.CLIENT, HibernateAspect.HIBERNATE);

		set(USERS, users);
		set(OFFSET, offset);
		set(TOTAL, total);
	}

	public List<User> getUsers() {
		return get(USERS);
	}

	public int getOffset() {
		return (Integer) get(OFFSET);
	}

	public long getTotal() {
		return (Long) get(TOTAL);
	}
}
