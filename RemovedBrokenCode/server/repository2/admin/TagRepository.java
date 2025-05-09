package ru.imagebook.server.repository2.admin;

import java.util.List;

import ru.imagebook.shared.model.site.Tag;

public interface TagRepository {
	List<Tag> loadTags();

	void saveTag(Tag tag);

	Tag getTag(int id);

	void deleteTags(List<Integer> ids);
}
