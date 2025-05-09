package ru.minogin.core.client.bean;

import ru.minogin.core.client.serialization.XSerializable;

public interface PersistentBean extends Bean, XSerializable {
	PersistentBean copy();
}
