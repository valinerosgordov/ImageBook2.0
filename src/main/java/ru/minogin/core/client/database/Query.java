package ru.minogin.core.client.database;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Query {
	public abstract String getSQL(Context context);

	public Collection<Object> getParameters() {
		return new ArrayList<Object>();
	}
}
