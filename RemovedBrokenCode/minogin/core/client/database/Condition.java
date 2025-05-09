package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Condition {
	protected abstract String getSQL(Context context);

	protected Collection<Object> getParameters() {
		return new ArrayList<Object>();
	}
}
