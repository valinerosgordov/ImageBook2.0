package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.site.Tag;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TagServiceAsync {
	void loadTags(AsyncCallback<List<Tag>> callback);

	void addTag(Tag tag, AsyncCallback<Void> callback);

	void updateTag(Tag tag, AsyncCallback<Void> callback);

	void deleteTags(List<Integer> ids, AsyncCallback<Void> callback);
}
