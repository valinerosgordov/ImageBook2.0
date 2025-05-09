package ru.imagebook.shared.model.site;

import java.util.Set;
import java.util.TreeSet;

import ru.imagebook.shared.model.Album;
import ru.minogin.core.client.bean.BaseEntityBean;

public class DirSection2 extends BaseEntityBean implements Comparable<DirSection2> {
	private static final long serialVersionUID = 369851637507043225L;

	public static final String INDEX = "index";
	public static final String KEY = "key";
	public static final String NAME = "name";
	public static final String PREVIEW = "preview";
	public static final String SECTION = "section";
	public static final String ALBUMS = "albums";

	public DirSection2() {
		setAlbums(new TreeSet<Album>());
	}

	public Integer getIndex() {
		return get(INDEX);
	}

	public void setIndex(Integer index) {
		set(INDEX, index);
	}

	public String getKey() {
		return get(KEY);
	}

	public void setKey(String key) {
		set(KEY, key);
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public String getPreview() {
		return get(PREVIEW);
	}

	public void setPreview(String preview) {
		set(PREVIEW, preview);
	}

	public DirSection1 getSection() {
		return get(SECTION);
	}

	public void setSection(DirSection1 section) {
		set(SECTION, section);
	}

	public Set<Album> getAlbums() {
		return get(ALBUMS);
	}

	public void setAlbums(Set<Album> albums) {
		set(ALBUMS, albums);
	}

	@Override
	public int compareTo(DirSection2 section) {
		if (getIndex() != null && section.getIndex() != null)
			return getIndex().compareTo(section.getIndex());
		else
			return getId().compareTo(section.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof DirSection2))
			return false;

		DirSection2 section = (DirSection2) obj;
		if (section.getId() == null || getId() == null)
			return false;

		return section.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
