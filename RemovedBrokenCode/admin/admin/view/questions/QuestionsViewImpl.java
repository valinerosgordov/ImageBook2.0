package ru.imagebook.client.admin.view.questions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.imagebook.shared.model.Question;
import ru.imagebook.shared.model.QuestionCategory;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.DateTimeField;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;
import ru.minogin.core.client.gxt.grid.BooleanColumnConfig;
import ru.minogin.core.client.gxt.grid.DateTimeColumnConfig;
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
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class QuestionsViewImpl implements QuestionsView {

	private QuestionsConstants constants;
	private CommonConstants appConstants;

	private QuestionsPresenter presenter;

	private ListStore<QuestionsModel> store;
	private XTextField nameField;
	private EmailField emailField;
	private DateTimeField dateField;
	private TextArea questionField;
	private TextArea answerField;
	private CheckBox publField;
	private SelectField<QuestionCategory> categoryField;
	private Window addWindow;
	private Grid<QuestionsModel> grid;
	private Window editWindow;

	private QuestionCategory questionCategory;

	private BasePagingLoader<PagingLoadResult<QuestionsModel>> loader;
	private AsyncCallback<PagingLoadResult<QuestionsModel>> questionsCallback;

	public QuestionsViewImpl() {
		constants = GWT.create(QuestionsConstants.class);
		appConstants = GWT.create(CommonConstants.class);
	}

	@Override
	public void setPresenter(QuestionsPresenter presenter) {
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

		loader = new BasePagingLoader<PagingLoadResult<QuestionsModel>>(
				new DataProxy<PagingLoadResult<QuestionsModel>>() {
					@Override
					public void load(DataReader<PagingLoadResult<QuestionsModel>> reader,
							Object loadConfig,
							AsyncCallback<PagingLoadResult<QuestionsModel>> callback) {
						questionsCallback = callback;
						PagingLoadConfig config = (PagingLoadConfig) loadConfig;
						presenter.loadQuestions(config.getOffset(), config.getLimit());
					}
				});

		store = new ListStore<QuestionsModel>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		ColumnConfig referendumAnswerColumnsConfig = new ColumnConfig(Question.QUESTION, constants.questionColumn(), 420);
		referendumAnswerColumnsConfig.setRenderer(new GridCellRenderer<QuestionsModel>() {
			@Override
			public Object render(QuestionsModel model, String property, ColumnData config, int rowIndex,
					int colIndex, ListStore<QuestionsModel> store, Grid<QuestionsModel> grid) {
				String value = model.get(property);
				return Format.htmlEncode(value);
			}
		});
		columns.add(referendumAnswerColumnsConfig);
		columns.add(new DateTimeColumnConfig(Question.DATE, constants.dateColumn(), 100));
		columns.add(new BooleanColumnConfig(Question.IS_ANSWERED, constants.answeredColumn(), 100));
		columns.add(new BooleanColumnConfig(Question.PUBL, constants.publColumn(), 100));

		grid = new Grid<QuestionsModel>(store, new ColumnModel(columns));

		FixedLiveGridView liveView = new FixedLiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);

		panel.add(grid);

		ToolBar bottomToolBar = new ToolBar();
		bottomToolBar.add(new FillToolItem());
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		bottomToolBar.add(item);
		panel.setBottomComponent(bottomToolBar);

		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FillLayout(Orientation.HORIZONTAL));
		container.add(panel);

		return container;
	}

	@Override
	public void updateQuestions() {
		loader.load();
	}

	@Override
	public void showQuestions(List<Question> questions, int offset, int totalCount) {
		List<QuestionsModel> resultList = new ArrayList<QuestionsModel>();
		for (Question question : questions) {
			resultList.add(new QuestionsModel(question));
		}
		PagingLoadResult<QuestionsModel> loadResult = new BasePagingLoadResult<QuestionsModel>(
				resultList, offset, (int) totalCount);
		questionsCallback.onSuccess(loadResult);
	}

	@Override
	public void showAddForm() {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setHeight(540);
		addWindow.setWidth(520);

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
		categoryField = new SelectField<QuestionCategory>(constants.category(), false, formPanel);

		dateField = new DateTimeField();
		dateField.setFieldLabel(constants.dateField());
		formPanel.add(dateField);

		nameField = new XTextField(constants.nameField(), true, formPanel);

		emailField = new EmailField(constants.emailField(), true, formPanel);

		questionField = new TextArea();
		questionField.setFieldLabel(constants.questionField());
		questionField.setAllowBlank(false);
		formPanel.add(questionField, new FormData(300, 150));

		answerField = new TextArea();
		answerField.setFieldLabel(constants.answerField());
		formPanel.add(answerField, new FormData(300, 150));

		publField = new CheckBox();
		publField.setFieldLabel(constants.publField());
		publField.setBoxLabel("");
		publField.addListener(Events.Change, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent ce) {
				if (Boolean.TRUE.equals(publField.getValue())) {
					answerField.setAllowBlank(false);
				}
				else{
					answerField.setAllowBlank(true);
				}
			}
		});
		formPanel.add(publField);
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
	public Question getSelectedQuestion() {
		QuestionsModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getQuestion();
	}

	@Override
	public void showEditForm() {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setHeight(540);
		editWindow.setWidth(520);

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
	public List<Question> getSelectedQuestions() {
		List<Question> questions = new ArrayList<Question>();
		List<QuestionsModel> items = grid.getSelectionModel().getSelectedItems();
		for (QuestionsModel model : items) {
			questions.add(model.getQuestion());
		}
		return questions;
	}

	@Override
	public void alertSelectDeleteQuestions() {
		MessageBox.alert(appConstants.warning(), constants.selectDeleteQuestions(),
				null);
	}

	@Override
	public void alertSelectEditQuestions() {
		MessageBox.alert(appConstants.warning(), constants.selectEditQuestions(),
				null);
	}

	@Override
	public void setQuestionCategory(QuestionCategory questionCategory) {
		this.questionCategory = questionCategory;
		if (questionCategory == null) {
			showQuestions(new ArrayList<Question>(), 0, 0);
		}
	}

	@Override
	public void fetch(Question question) {
		question.setQuestionCategory(categoryField.getXValue());
		question.setAnswer(answerField.getValue());
		question.setDate(dateField.getValue());
		question.setEmail(emailField.getValue());
		question.setName(nameField.getValue());
		question.setPubl(publField.getValue());
		question.setQuestion(questionField.getValue());
	}

	@Override
	public void reload() {
		loader.load();
	}

	@Override
	public void reloadFull() {
		loader.setOffset(0);
		reload();
	}

	@Override
	public void setNewQuestionFieldValues(Date date){
		this.dateField.setValue(date);
		this.categoryField.setXValue(presenter.getQuestionCategory());
	}

	@Override
	public void fillCategorySelect(List<QuestionCategory> categories) {
	//categoryField.add(presenter.getDummyCategory(), presenter.getDummyCategory().getName());
		for (QuestionCategory qc : categories) {
			categoryField.add(qc, qc.getName());
		}
	}

	@Override
	public void setFormValues(Question question){
		this.categoryField.setXValue(presenter.getQuestionCategory());
		this.answerField.setValue(question.getAnswer());
		this.dateField.setValue(question.getDate());
		this.emailField.setValue(question.getEmail());
		this.nameField.setValue(question.getName());
		this.publField.setValue(question.isPubl());
		this.questionField.setValue(question.getQuestion());
	}

}
