package ru.imagebook.shared.model.site;

import java.util.Set;
import java.util.TreeSet;

import ru.minogin.core.client.bean.BaseEntityBean;

public class Folder extends BaseEntityBean {
	private static final long serialVersionUID = 2900455397788716089L;

	public static final String NAME = "name";
	public static final String DOCUMENTS = "documents";

	public Folder() {
		setDocuments(new TreeSet<Document>());
	}

	public String getName() {
		return get(NAME);
	}

	public void setName(String name) {
		set(NAME, name);
	}

	public Set<Document> getDocuments() {
		return get(DOCUMENTS);
	}

	public void setDocuments(Set<Document> documents) {
		set(DOCUMENTS, documents);
	}
}
