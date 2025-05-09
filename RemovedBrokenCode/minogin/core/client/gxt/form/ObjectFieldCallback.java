package ru.minogin.core.client.gxt.form;

import java.util.List;

public interface ObjectFieldCallback<T> {
	void onLoaded(List<T> objects, int offset, long total);
}
