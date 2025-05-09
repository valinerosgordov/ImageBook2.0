package ru.imagebook.shared.model.site;

public class DocumentImpl extends PageImpl implements Document {
	private static final long serialVersionUID = 2990942677902954003L;

	@Override
	public Folder getFolder() {
		return get(FOLDER);
	}

	@Override
	public void setFolder(Folder folder) {
		set(FOLDER, folder);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof Document))
			return false;

		Document document = (Document) obj;
		if (document.getId() == null || getId() == null)
			return false;

		return document.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}

	@Override
	public int compareTo(Document document) {
		return (getName() != null && document.getName() != null) ? getName().compareTo(
				document.getName()) : getId().compareTo(document.getId());
	}
}
