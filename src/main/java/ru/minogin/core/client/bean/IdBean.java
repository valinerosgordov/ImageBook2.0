package ru.minogin.core.client.bean;

import ru.minogin.core.client.model.Identifiable;

public interface IdBean extends Bean, Identifiable {
	void setId(String id);
}
