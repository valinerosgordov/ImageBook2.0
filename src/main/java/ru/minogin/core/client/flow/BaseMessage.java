package ru.minogin.core.client.flow;

import java.util.HashSet;
import java.util.Set;

import ru.minogin.core.client.bean.BaseBean;

public class BaseMessage extends BaseBean implements Message {
	private static final long serialVersionUID = -6074461018430586998L;

	public static final String MESSAGE_TYPE = "message_type";
	public static final String ASPECTS = "message_aspects";
	public static final String FROM = "message_from";
	public static final String TO = "message_to";
	public static final String CANCELLED = "message_cancelled";

	protected BaseMessage() {}

	public BaseMessage(String type) {
		set(MESSAGE_TYPE, type);
		set(ASPECTS, new HashSet<String>());
	}

	@Override
	public String getMessageType() {
		return get(MESSAGE_TYPE);
	}

	@Override
	public boolean is(String type) {
		return getMessageType().equals(type);
	}

	@Override
	public boolean hasAspect(String aspect) {
		return getAspects().contains(aspect);
	}

	@Override
	public void addAspects(String... aspects) {
		for (String aspect : aspects)
			getAspects().add(aspect);
	}

	public Set<String> getAspects() {
		return get(ASPECTS);
	}

	@Override
	public String getTo() {
		return get(TO);
	}

	@Override
	public void setTo(String to) {
		set(TO, to);
	}

	@Override
	public String getFrom() {
		return get(FROM);
	}

	@Override
	public void setFrom(String from) {
		set(FROM, from);
	}

	@Override
	public boolean isCancelled() {
		Boolean cancelled = get(CANCELLED);
		return cancelled != null ? cancelled : false;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		set(CANCELLED, cancelled);
	}

	@Override
	public void cancel() {
		setCancelled(true);
	}

	@Override
	public void reply(Message request) {
		setTo(request.getFrom());
	}
}
