package ru.imagebook.server.service2.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.TagService;
import ru.imagebook.server.repository2.admin.TagRepository;
import ru.imagebook.shared.model.site.Tag;

public class TagServiceImpl implements TagService {
	private final TagRepository repository;

	@Autowired
	public TagServiceImpl(TagRepository repository) {
		this.repository = repository;
	}

	@Transactional
	@PreAuthorize("hasRole('SITE_ADMIN')")
	@Override
	public List<Tag> loadTags() {
		return repository.loadTags();
	}

	@Transactional
	@PreAuthorize("hasRole('SITE_ADMIN')")
	@Override
	public void addTag(Tag tag) {
		repository.saveTag(tag);
	}

	@Transactional
	@PreAuthorize("hasRole('SITE_ADMIN')")
	@Override
	public void updateTag(Tag modified) {
		Tag tag = repository.getTag(modified.getId());
		tag.setName(modified.getName());
	}

	@Transactional
	@PreAuthorize("hasRole('SITE_ADMIN')")
	@Override
	public void deleteTags(List<Integer> ids) {
		repository.deleteTags(ids);
	}
}
