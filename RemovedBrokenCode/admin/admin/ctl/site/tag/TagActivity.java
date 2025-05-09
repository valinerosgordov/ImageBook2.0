package ru.imagebook.client.admin.ctl.site.tag;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.TagServiceAsync;
import ru.imagebook.client.admin.view.site.tag.TagPresenter;
import ru.imagebook.client.admin.view.site.tag.TagView;
import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.client.app.failure.XAsyncCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TagActivity extends AbstractActivity implements TagPresenter {
	private final TagView view;

	@Inject
	private TagServiceAsync service;

	private Tag tag;

	@Inject
	public TagActivity(TagView view) {
		view.setPresenter(this);
		this.view = view;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		showTags();
	}

	private void showTags() {
		service.loadTags(new XAsyncCallback<List<Tag>>() {
			@Override
			public void onSuccess(List<Tag> tags) {
				view.showTags(tags);
			}
		});
	}

	@Override
	public void addButtonClicked() {
		view.showAddForm();
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		Tag tag = new Tag();
		tag.setName(view.getName());
		service.addTag(tag, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				showTags();
			}
		});
	}

	@Override
	public void editButtonClicked() {
		tag = view.getSelectedTag();
		view.showEditForm();
		view.setName(tag.getName());
	}

	@Override
	public void saveButtonClickedOnEditForm() {
		tag.setName(view.getName());
		service.updateTag(tag, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				showTags();
			}
		});
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedTags().isEmpty())
			view.alertSelectTags();
		else
			view.confirmDelete();
	}

	@Override
	public void deleteConfirmed() {
		List<Tag> tags = view.getSelectedTags();
		List<Integer> ids = new ArrayList<Integer>();
		for (Tag tag : tags) {
			ids.add(tag.getId());
		}
		service.deleteTags(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				showTags();
			}
		});
	}
}
