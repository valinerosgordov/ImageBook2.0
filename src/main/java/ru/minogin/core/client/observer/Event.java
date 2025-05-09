package ru.minogin.core.client.observer;

import ru.minogin.core.client.rpc.Transportable;

public class Event implements Transportable {
	private static final long serialVersionUID = 41173261554330693L;

	private String type;
	private boolean removeListener;

	protected Event() {}

	public Event(String type) {
		if (type == null)
			throw new NullPointerException();
		
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	protected void setType(String type) {
		this.type = type;
	}

	public boolean removeListener() {
		return removeListener;
	}

	public void setRemoveListener(boolean removeListener) {
		this.removeListener = removeListener;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Event))
			return false;

		Event event = (Event) obj;
		return type.equals(event.type);
	}

	@Override
	public int hashCode() {
		return type.hashCode();
	}
	
	@Override
	public String toString() {
		return "{" + type + "}";
	}
}
