package ru.imagebook.client.admin.view.site.tag;

import ru.imagebook.shared.model.site.Tag;

import com.extjs.gxt.ui.client.data.BaseModel;

public class TagModel extends BaseModel {
	private static final long serialVersionUID = -3100574256646349259L;

	private final Tag tag;

	public TagModel(Tag tag) {
		this.tag = tag;

		set(Tag.NAME, tag.getName());
	}

	public Tag getTag() {
		return tag;
	}
}
