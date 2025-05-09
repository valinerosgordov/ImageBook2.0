package ru.imagebook.server.service2.admin;

import static ru.minogin.core.client.text.StringUtil.nullIfEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.server.repository2.admin.SiteRepository;
import ru.imagebook.shared.model.site.Section;
import ru.imagebook.shared.model.site.Tag;

public class SiteServiceImpl implements SiteService {
	private final SiteRepository repository;

	@Autowired
	public SiteServiceImpl(SiteRepository repository) {
		this.repository = repository;
	}

	@Transactional
	@Override
	public Section loadSections() {
		Section root = null;
		Map<Section, List<Section>> map = new HashMap<Section, List<Section>>();
		List<Section> sections = repository.loadSections();
		for (Section section : sections) {
			Section parent = section.getParent();
			if (parent != null) {
				List<Section> parentSections = map.get(parent);
				if (parentSections == null) {
					parentSections = new ArrayList<Section>();
					map.put(parent, parentSections);
				}
				parentSections.add(section);
			}
			else
				root = section;
		}

		addChildren(root, map);

		return root;
	}

	private void addChildren(Section section, Map<Section, List<Section>> map) {
		List<Section> children = map.get(section);
		if (children != null) {
			section.getChildren().addAll(children);
			for (Section child : children) {
				addChildren(child, map);
			}
		}
	}

	@Transactional
	@Override
	public Section updateSection(int id, String h1, String key, String title, String footer, int tagId) {
		Section section = repository.getSection(id);
		section.setH1(nullIfEmpty(h1));
		section.setKey(nullIfEmpty(key));
		section.setTitle(nullIfEmpty(title));
		section.setFooter(nullIfEmpty(footer));

		Tag tag = null;
		if (tagId != 0)
			tag = repository.getTag(tagId);
		section.setTag(tag);

		return section;
	}

	@Transactional
	@Override
	public List<Tag> loadTags() {
		return repository.loadTags();
	}
}
