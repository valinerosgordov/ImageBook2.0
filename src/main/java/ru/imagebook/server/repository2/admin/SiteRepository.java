package ru.imagebook.server.repository2.admin;

import java.util.List;

import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.Tag;

public interface SiteRepository {
	List<Section> loadSections();

	Section getSection(int id);

	List<Tag> loadTags();

	Tag getTag(int tagId);
}
