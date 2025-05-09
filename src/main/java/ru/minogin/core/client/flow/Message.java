package ru.minogin.core.client.flow;

import ru.minogin.core.client.bean.Bean;

public interface Message extends Bean {
	public String getMessageType();

	boolean is(String type);

	boolean hasAspect(String aspect);

	void addAspects(String... aspects);

	String getFrom();

	void setFrom(String from);

	String getTo();

	void setTo(String to);

	boolean isCancelled();

	void setCancelled(boolean cancelled);

	void cancel();

	void reply(Message message);
}
