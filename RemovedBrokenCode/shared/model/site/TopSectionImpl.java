package ru.imagebook.shared.model.site;

public class TopSectionImpl extends PageImpl implements TopSection {
	private static final long serialVersionUID = 6557815943269441433L;

	public TopSectionImpl() {
		setNumber(0);
	}

	public Integer getNumber() {
		return get(NUMBER);
	}

	public void setNumber(Integer number) {
		set(NUMBER, number);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (!(obj instanceof TopSection))
			return false;

		TopSection topSection = (TopSection) obj;
		if (topSection.getId() == null || getId() == null)
			return false;

		return topSection.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return getId() != null ? getId().hashCode() : super.hashCode();
	}
}
