package ru.imagebook.client.admin.view.questions;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.IntegerField;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;
import ru.minogin.core.client.gxt.grid.FixedLiveGridView;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class QuestionCategoryViewImpl implements QuestionCategoryView {

	private QuestionCategoryConstants constants = GWT
			.create(QuestionCategoryConstants.class);

	private CommonConstants appConstants = GWT.create(CommonConstants.class);

	private QuestionCategoryPresenter presenter;

	private ListStore<QuestionCategoryModel> store;
	private XTextField questionCategoryNameField;
	private IntegerField questionCategoryNumberField;
	private Window addWindow;
	private Grid<QuestionCategoryModel> grid;
	private Window editWindow;

	private BasePagingLoader<PagingLoadResult<QuestionCategoryModel>> loader;
	private AsyncCallback<PagingLoadResult<QuestionCategoryModel>> questionCategoryCallback;

	private QuestionsView questionView;

	@Override
	public void setPresenter(QuestionCategoryPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public Widget asWidget() {
		ContentPanel panel = new XContentPanel(constants.heading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.addButtonClicked();
					}
				}));
		toolBar.add(new Button(appConstants.edit(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.editButtonClicked();
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.deleteButtonClicked();
					}
				}));
		panel.setTopComponent(toolBar);

		loader = new BasePagingLoader<PagingLoadResult<QuestionCategoryModel>>(
				new DataProxy<PagingLoadResult<QuestionCategoryModel>>() {
					@Override
					public void load(
							DataReader<PagingLoadResult<QuestionCategoryModel>> reader,
							Object loadConfig,
							AsyncCallback<PagingLoadResult<QuestionCategoryModel>> callback) {
						questionCategoryCallback = callback;
						PagingLoadConfig config = (PagingLoadConfig) loadConfig;
						presenter.loadQuestionCategories(config.getOffset(),
								config.getLimit());
					}
				});

		store = new ListStore<QuestionCategoryModel>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(QuestionCategory.NAME, constants.nameColumn(),
				200));
		columns.add(new ColumnConfig(QuestionCategory.NUMBER, constants
				.numberColumn(), 150));

		grid = new Grid<QuestionCategoryModel>(store, new ColumnModel(columns));

		LiveGridView liveView = new FixedLiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);

		grid.getSelectionModel().addSelectionChangedListener(
				new SelectionChangedListener<QuestionCategoryModel>() {
					@Override
					public void selectionChanged(
							SelectionChangedEvent<QuestionCategoryModel> se) {
						presenter.questionCategoryClicked();
					}
				});

		panel.add(grid);

		ToolBar bottomToolBar = new ToolBar();
		bottomToolBar.add(new FillToolItem());
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		bottomToolBar.add(item);
		panel.setBottomComponent(bottomToolBar);

		questionView = presenter.getQuestionsPresenter().getQuestionsView();

		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FillLayout(Orientation.HORIZONTAL));
		container.add(panel);
		container.add(questionView.asWidget());

		return container;
	}

	@Override
	public void updateQuestionCategories() {
		loader.load();
	}

	@Override
	public void showQuestionCategories(List<QuestionCategory> questionCategories,
			int offset, int totalCount) {
		List<QuestionCategoryModel> resultList = new ArrayList<QuestionCategoryModel>(questionCategories.size() + 1);
		
		QuestionCategoryModel nullModel = new QuestionCategoryModel(presenter.getDummyCategory());
		resultList.add(nullModel);
		
		for (QuestionCategory questionCategory : questionCategories) {
			resultList.add(new QuestionCategoryModel(questionCategory));
		}
		PagingLoadResult<QuestionCategoryModel> loadResult = new BasePagingLoadResult<QuestionCategoryModel>(
				resultList, offset, (int) totalCount);
		questionCategoryCallback.onSuccess(loadResult);
					
	}

	@Override
	public void showAddForm() {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel);

		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.saveButtonClickedOnAddForm();
					}
				});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(appConstants.cancel(),
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
		questionCategoryNameField = new XTextField(constants.nameField(), false,
				formPanel);
		questionCategoryNumberField = new IntegerField(constants.numberField(),
				false, formPanel);
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public void fetch(QuestionCategory questionCategory) {
		questionCategory.setName(questionCategoryNameField.getValue());
		questionCategory.setNumber(questionCategoryNumberField.getValue());
	}

	@Override
	public void setFormValues(QuestionCategory questionCategory) {
		this.questionCategoryNameField.setValue(questionCategory.getName());
		this.questionCategoryNumberField.setValue(questionCategory.getNumber());
	}

	@Override
	public QuestionCategory getSelectedQuestionCategory() {
		QuestionCategoryModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getQuestionCategory();
	}

	@Override
	public void showEditForm() {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel);

		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.saveButtonClickedOnEditForm();
					}
				});
		formPanel.addButton(saveButton);
		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(appConstants.cancel(),
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
		new ConfirmMessageBox(appConstants.warning(), constants.confirmDelete(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						presenter.deleteConfirmed();
					}
				});
	}

	@Override
	public List<QuestionCategory> getSelectedQuestionCategories() {
		List<QuestionCategory> questionCategories = new ArrayList<QuestionCategory>();
		List<QuestionCategoryModel> items = grid.getSelectionModel()
				.getSelectedItems();
		for (QuestionCategoryModel model : items) {
			questionCategories.add(model.getQuestionCategory());
		}
		return questionCategories;
	}

	@Override
	public void alertSelectDeleteCountries() {
		MessageBox.alert(appConstants.warning(),
				constants.selectDeleteCategories(), null);
	}

	@Override
	public void alertSelectEditCountries() {
		MessageBox.alert(appConstants.warning(), constants.selectEditCategories(),
				null);
	}

	@Override
	public QuestionsView getQuestionsView() {
		return questionView;
	}
}
