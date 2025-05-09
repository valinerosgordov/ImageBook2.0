package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.site.Tag;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("tag.remoteService")
public interface TagService extends RemoteService {
	List<Tag> loadTags();

	void addTag(Tag tag);

	void updateTag(Tag tag);

	void deleteTags(List<Integer> ids);
}
