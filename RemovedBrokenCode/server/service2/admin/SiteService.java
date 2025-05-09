package ru.imagebook.server.service2.admin;

import java.util.List;

import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.Tag;

public interface SiteService {
	Section loadSections();

	Section updateSection(int id, String h1, String key, String title, String footer, int tagId);

	List<Tag> loadTags();
}
