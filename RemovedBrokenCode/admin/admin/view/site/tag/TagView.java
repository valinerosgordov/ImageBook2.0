package ru.imagebook.client.admin.view.site.tag;

import java.util.List;

import ru.imagebook.shared.model.site.Tag;

import com.google.gwt.user.client.ui.IsWidget;

public interface TagView extends IsWidget {

	void showTags(List<Tag> tags);

	void showAddForm();

	String getName();

	void setName(String name);

	void hideAddForm();

	Tag getSelectedTag();

	void showEditForm();

	void hideEditForm();

	void confirmDelete();

	List<Tag> getSelectedTags();

	void alertSelectTags();

	void setPresenter(TagPresenter presenter);
}
