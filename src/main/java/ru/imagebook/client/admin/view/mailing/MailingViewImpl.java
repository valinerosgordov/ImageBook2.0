package ru.imagebook.client.admin.view.mailing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import ru.imagebook.client.admin.ctl.mailing.AddMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.DeleteMailingsMessage;
import ru.imagebook.client.admin.ctl.mailing.MailingView;
import ru.imagebook.client.admin.ctl.mailing.SendMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.TestMailingMessage;
import ru.imagebook.client.admin.ctl.mailing.UpdateMailingMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Mailing;
import ru.imagebook.shared.model.MailingState;
import ru.imagebook.shared.model.MailingType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.BeanModel;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextArea;
import ru.minogin.core.client.gxt.form.XTextField;
import ru.minogin.core.client.gxt.grid.DateColumnConfig;
import ru.minogin.core.client.gxt.grid.IntegerColumnConfig;
import ru.minogin.core.client.text.StringUtil;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class MailingViewImpl extends View implements MailingView {
	private static final String ERRORS = "errors";

	private final Widgets widgets;
	private final MailingConstants constants;
	private final CommonConstants appConstants;
    private final UserService userService;

	private XTextField nameField;
    private XTextField nameFromField;
    private EmailField emailFromField;
	private XTextField subjectField;
	private XTextArea contentField;
	private SelectField<Integer> typeField;
	private Grid<BeanModel<Mailing>> grid;
	private XWindow editWindow;
	private XWindow addWindow;
	private ListStore<BeanModel<Mailing>> store;

	private XWindow testWindow;

	private Button sendButton;

	@Inject
	public MailingViewImpl(Dispatcher dispatcher, Widgets widgets,
			MailingConstants constants, CommonConstants appConstants,
            UserService userService) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
        this.userService = userService;
	}

	@Override
	public void showSection(final String locale, final String email) {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
		desktop.removeAll();

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeading(constants.panelHeading());
		desktop.add(panel);

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						showAddForm();
					}
				}));
		toolBar.add(new Button(appConstants.edit(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						BeanModel<Mailing> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Mailing mailing = item.getBean();
							showEditForm(mailing);
						}
					}
				}));
		toolBar.add(new Button(appConstants.delete(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final List<BeanModel<Mailing>> items = grid.getSelectionModel()
								.getSelectedItems();
						if (!items.isEmpty()) {
							new ConfirmMessageBox(appConstants.warning(), constants
									.confirmDelete(), new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									List<Integer> ids = new ArrayList<Integer>();
									for (BeanModel<Mailing> item : items) {
										ids.add(item.getBean().getId());
									}
									send(new DeleteMailingsMessage(ids));
								}
							});
						}
					}
				}));
		toolBar.add(new Button(constants.testButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						BeanModel<Mailing> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Mailing mailing = item.getBean();
							if (mailing.getState() == MailingState.NEW)
								showTestForm(mailing, email);
						}
					}
				}));

		sendButton = new Button(constants.sendButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						final BeanModel<Mailing> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							final Mailing mailing = item.getBean();
							if (mailing.getState() == MailingState.NEW) {
								new ConfirmMessageBox(appConstants.warning(),
										constants.confirmSend(), new Listener<BaseEvent>() {
											@Override
											public void handleEvent(BaseEvent be) {
												sendButton.disable();
												send(new SendMailingMessage(mailing.getId()));
											}
										});
							}
						}
					}
				});
		toolBar.add(sendButton);

		toolBar.add(new Button(constants.reportButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						BeanModel<Mailing> item = grid.getSelectionModel()
								.getSelectedItem();
						if (item != null) {
							Mailing mailing = item.getBean();
							if (mailing.getState() == MailingState.SENT)
								showReport(mailing);
						}
					}
				}));
		panel.setTopComponent(toolBar);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new DateColumnConfig(Mailing.DATE, constants.dateColumn(), 70));
		columns.add(new ColumnConfig(Mailing.NAME, constants.nameColumn(), 200));

		ColumnConfig columnConfig = new ColumnConfig(Mailing.TYPE,
				constants.typeColumn(), 200);
		columnConfig.setRenderer(new GridCellRenderer<BeanModel<Mailing>>() {
			@Override
			public Object render(BeanModel<Mailing> model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel<Mailing>> store, Grid<BeanModel<Mailing>> grid) {
				int type = (Integer) model.get(property);
				return MailingType.values.get(type);
			}
		});
		columns.add(columnConfig);

		columnConfig = new ColumnConfig(Mailing.STATE, constants.stateColumn(), 100);
		columnConfig.setRenderer(new GridCellRenderer<BeanModel<Mailing>>() {
			@Override
			public Object render(BeanModel<Mailing> model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<BeanModel<Mailing>> store, Grid<BeanModel<Mailing>> grid) {
				int state = (Integer) model.get(property);
				return MailingState.values.get(state).get(locale);
			}
		});
		columns.add(columnConfig);

		columns.add(new IntegerColumnConfig(Mailing.SENT, constants.sentColumn(),
				80));
		columns.add(new IntegerColumnConfig(ERRORS, constants.errorsColumn(), 50));
		columns.add(new IntegerColumnConfig(Mailing.TOTAL, constants.totalColumn(),
				50));

		store = new ListStore<BeanModel<Mailing>>();

		grid = new Grid<BeanModel<Mailing>>(store, new ColumnModel(columns));
		panel.add(grid);

		desktop.layout();
	}

	protected void showReport(Mailing mailing) {
		final XWindow window = new XWindow();
		window.setHeading(constants.reportHeading());

		ContentPanel panel = new ContentPanel();
		panel.setHeaderVisible(false);
		panel.setScrollMode(Scroll.AUTO);

		String report = mailing.getReport();
		report = StringUtil.nlToBr(report);

		Html html = new Html(report);
		html.setStyleAttribute("margin", "5px");
		panel.add(html);

		window.addButton(new Button(appConstants.close(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						window.hide();
					}
				}));

		window.add(panel);

		window.show();
	}

	private void showAddForm() {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setWidth(700);

        final User user = userService.getUser();
        final Vendor vendor = user.getVendor();

		XFormPanel formPanel = new XFormPanel();
		formPanel.setFieldWidth(400);
		addFields(formPanel, new Mailing(), vendor, true);

		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Mailing mailing = new Mailing();
						fetch(mailing);
						send(new AddMailingMessage(mailing));
					}
				});
		formPanel.addButton(saveButton);

		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						addWindow.hide();
					}
				}));

		new FormButtonBinding(formPanel).addButton(saveButton);

		addWindow.add(formPanel);

		addWindow.show();
	}

	protected void showEditForm(final Mailing mailing) {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setWidth(700);

		XFormPanel formPanel = new XFormPanel();
		formPanel.setFieldWidth(400);
		addFields(formPanel, mailing, null, false);

		if (mailing.getState() == MailingState.NEW) {
			Button saveButton = new Button(appConstants.save(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							fetch(mailing);
							send(new UpdateMailingMessage(mailing));
						}
					});
			formPanel.addButton(saveButton);
			new FormButtonBinding(formPanel).addButton(saveButton);
		}

		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						editWindow.hide();
					}
				}));

		editWindow.add(formPanel);

		editWindow.show();
	}

	protected void fetch(Mailing mailing) {
		mailing.setName(nameField.getValue());
        mailing.setNameFrom(nameFromField.getValue());
        mailing.setEmailFrom(emailFromField.getValue());
		mailing.setSubject(subjectField.getValue());
		mailing.setContent(contentField.getValue());
		mailing.setType(typeField.getXValue());
	}

	private void addFields(FormPanel formPanel, Mailing mailing, Vendor vendor, boolean isAddForm) {
        String nameFrom = isAddForm ? vendor.getName() : mailing.getNameFrom();
        String emailFrom = isAddForm ? vendor.getEmail() : mailing.getEmailFrom();

        nameField = new XTextField(constants.nameField(), false, mailing.getName(), formPanel);
        nameFromField = new XTextField(constants.nameFromField(), false, nameFrom, formPanel);
        emailFromField = new EmailField(constants.emailFromField(), false, emailFrom, formPanel);
		subjectField = new XTextField(constants.subjectField(), false, mailing.getSubject(), formPanel);
		contentField = new XTextArea(constants.contentField(), false, mailing.getContent(), formPanel);
		contentField.setHeight(300);
		typeField = new SelectField<Integer>(constants.typeField(), false, formPanel);
		for (Entry<Integer, String> entry : MailingType.values.entrySet()) {
			typeField.add(entry.getKey(), entry.getValue());
		}
		typeField.setXValue(mailing.getType());
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public void hideEditForm() {
		editWindow.hide();
	}

	@Override
	public void showMailings(List<Mailing> mailings) {
		store.removeAll();

		for (Mailing mailing : mailings) {
			BeanModel<Mailing> model = new BeanModel<Mailing>(mailing);
			model.setTransient(ERRORS, mailing.getTotal() - mailing.getSent());
			store.add(model);
		}
	}

	private void showTestForm(final Mailing mailing, String email) {
		testWindow = new XWindow(constants.testHeading());
		testWindow.setHeight(150);

		XFormPanel formPanel = new XFormPanel();

		final EmailField emailField = new EmailField(constants.emailField(), false,
				email, formPanel);

		Button testButton = new Button(constants.testMailingButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(new TestMailingMessage(mailing.getId(), emailField.getValue()));
					}
				});
		formPanel.addButton(testButton);

		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						testWindow.hide();
					}
				}));

		new FormButtonBinding(formPanel).addButton(testButton);

		testWindow.add(formPanel);

		testWindow.show();
	}

	@Override
	public void hideTestForm() {
		testWindow.hide();
	}

	@Override
	public void enableSendButton() {
		sendButton.enable();
	}
}
