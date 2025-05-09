package ru.imagebook.client.admin.view.site.tag;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.site.Tag;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TagViewImpl implements TagView {
	@Inject
	private TagConstants constants;
	@Inject
	private CommonConstants commonConstants;

	private TagPresenter presenter;

	private ListStore<TagModel> store;
	private XTextField nameField;
	private Window addWindow;
	private Grid<TagModel> grid;
	private Window editWindow;

	@Override
	public void setPresenter(TagPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		ContentPanel panel = new XContentPanel(constants.heading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(commonConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.addButtonClicked();
					}
				}));
		toolBar.add(new Button(commonConstants.edit(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.editButtonClicked();
					}
				}));
		toolBar.add(new Button(commonConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.deleteButtonClicked();
					}
				}));
		panel.setTopComponent(toolBar);

		store = new ListStore<TagModel>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Tag.NAME, constants.nameColumn(), 200));

		grid = new Grid<TagModel>(store, new ColumnModel(columns));
		panel.add(grid);

		return panel;
	}

	@Override
	public void showTags(List<Tag> tags) {
		store.removeAll();

		for (Tag tag : tags) {
			store.add(new TagModel(tag));
		}
	}

	@Override
	public void showAddForm() {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel);

		Button saveButton = new Button(commonConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.saveButtonClickedOnAddForm();
					}
				});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(commonConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						hideAddForm();
					}
				}));

		addWindow.add(formPanel);

		addWindow.show();
	}

	private void addFields(FormPanel formPanel) {
		nameField = new XTextField(constants.nameField(), false, formPanel);
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public String getName() {
		return nameField.getValue();
	}

	@Override
	public void setName(String name) {
		nameField.setValue(name);
	}

	@Override
	public Tag getSelectedTag() {
		TagModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getTag();
	}

	@Override
	public void showEditForm() {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel);

		Button saveButton = new Button(commonConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.saveButtonClickedOnEditForm();
					}
				});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(commonConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						hideEditForm();
					}
				}));

		editWindow.add(formPanel);

		editWindow.show();
	}

	@Override
	public void hideEditForm() {
		editWindow.hide();
	}

	@Override
	public void confirmDelete() {
		new ConfirmMessageBox(commonConstants.warning(), constants.confirmDelete(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						presenter.deleteConfirmed();
					}
				});
	}

	@Override
	public List<Tag> getSelectedTags() {
		List<Tag> tags = new ArrayList<Tag>();
		List<TagModel> items = grid.getSelectionModel().getSelectedItems();
		for (TagModel model : items) {
			tags.add(model.getTag());
		}
		return tags;
	}

	@Override
	public void alertSelectTags() {
		MessageBox.alert(commonConstants.warning(), constants.selectTags(), null);
	}
}
