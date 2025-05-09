package ru.imagebook.client.admin.view.vendor;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.vendor.AddVendorMessage;
import ru.imagebook.client.admin.ctl.vendor.EditVendorMessage;
import ru.imagebook.client.admin.ctl.vendor.UpdateVendorMessage;
import ru.imagebook.client.admin.ctl.vendor.VendorMessages;
import ru.imagebook.client.admin.ctl.vendor.VendorView;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.HintPlugin;
import ru.minogin.core.client.gxt.form.BooleanField;
import ru.minogin.core.client.gxt.form.EmailField;
import ru.minogin.core.client.gxt.form.SelectField;

@Singleton
public class VendorViewImpl extends View implements VendorView {
	private final Widgets widgets;
	private final VendorConstants constants;
	private ListStore<VendorModel> store;
	private final CommonConstants appConstants;
	private Window addWindow;
	private Grid<VendorModel> grid;
	private TextField<String> nameField;
	private TextField<String> customerIdField;
	private Window editWindow;
	private TextField<String> companyNameField;
	private EmailField emailField;
	private TextField<String> phoneField;
	private TextField<String> siteField;
	private TextField<String> colorField;
	private EmailField adminEmailField;
	private SelectField<Integer> typeField;
	private BooleanField printerField;
	private TextField<String> keyField;
	private TextField<String> orderImportPasswdField;
	private TextField<String> receiverField;
	private TextField<String> innField;
	private TextField<String> kppField;
	private TextField<String> accountField;
	private TextField<String> bankField;
	private TextField<String> bikField;
	private TextField<String> corrAccountField;
	private TextField<String> roboLoginField;
	private TextField<String> roboPassword1Field;
	private TextField<String> roboPassword2Field;
	private TextField<String> smsFromField;
    private NumberField yandexShopIdField;
    private NumberField yandexScidField;
	private TextField<String> yandexShopPasswordField;

    @Inject
	public VendorViewImpl(Dispatcher dispatcher, Widgets widgets,
			VendorConstants constants, CommonConstants appConstants) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showSection() {
		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
		desktop.removeAll();

		ContentPanel panel = new ContentPanel(new FitLayout());
		panel.setHeading(constants.heading());

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(appConstants.add(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						send(VendorMessages.SHOW_ADD_FORM);
					}
				}));
		toolBar.add(new Button(appConstants.edit(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						VendorModel item = grid.getSelectionModel().getSelectedItem();
						if (item != null) {
							Vendor agent = item.getEntity();
							send(new EditVendorMessage(agent));
						}
					}
				}));
		panel.setTopComponent(toolBar);

		store = new ListStore<VendorModel>();

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Vendor.NAME, constants.nameColumn(), 200));
		columns.add(new ColumnConfig(Vendor.CUSTOMER_ID, constants
				.customerIdColumn(), 100));
		columns.add(new ColumnConfig(Vendor.PRINTER, constants.isPrinterColumn(), 100));

		grid = new Grid<VendorModel>(store, new ColumnModel(columns));
		panel.add(grid);

		desktop.add(panel);

		desktop.layout();
	}

	@Override
	public void showAgents(List<Vendor> agents) {
		store.removeAll();

		for (Vendor agent : agents) {
			store.add(new VendorModel(agent));
		}
	}

	@Override
	public void showAddForm() {
		addWindow = new Window();
		addWindow.setLayout(new FitLayout());
		addWindow.setModal(true);
		addWindow.setOnEsc(false);
		addWindow.setSize(700, 500);
		addWindow.setHeading(constants.addWindowHeading());

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(200);
		formPanel.setScrollMode(Scroll.AUTO);

		addFields(formPanel, true);

		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						Vendor vendor = new Vendor();
						fetch(vendor);
						send(new AddVendorMessage(vendor));
					}
				});
		formPanel.addButton(saveButton);

		new FormButtonBinding(formPanel).addButton(saveButton);

		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						addWindow.hide();
					}
				}));

		addWindow.add(formPanel);

		addWindow.show();
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public void showEditForm(final Vendor vendor) {
		editWindow = new Window();
		editWindow.setLayout(new FitLayout());
		editWindow.setModal(true);
		editWindow.setOnEsc(false);
		editWindow.setSize(700, 500);
		editWindow.setHeading(constants.editWindowHeading());

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(200);
		formPanel.setScrollMode(Scroll.AUTO);

		addFields(formPanel, false);

		nameField.setValue(vendor.getName());
		typeField.setXValue(vendor.getType());
		printerField.setValue(vendor.isPrinter());
		customerIdField.setValue(vendor.getCustomerId());
		keyField.setValue(vendor.getKey());		
		companyNameField.setValue(vendor.getCompanyName());
		emailField.setValue(vendor.getEmail());
		phoneField.setValue(vendor.getPhone());
		siteField.setValue(vendor.getSite());
		colorField.setValue(vendor.getColor());
		adminEmailField.setValue(vendor.getAdminEmail());

		receiverField.setValue(vendor.getReceiver());
		innField.setValue(vendor.getInn());
		kppField.setValue(vendor.getKpp());
		accountField.setValue(vendor.getAccount());
		corrAccountField.setValue(vendor.getCorrAccount());
		bankField.setValue(vendor.getBank());
		bikField.setValue(vendor.getBik());

		roboLoginField.setValue(vendor.getRoboLogin());
		roboPassword1Field.setValue(vendor.getRoboPassword1());
		roboPassword2Field.setValue(vendor.getRoboPassword2());

		smsFromField.setValue(vendor.getSmsFrom());

        yandexShopIdField.setValue(vendor.getYandexShopId());
        yandexScidField.setValue(vendor.getYandexScid());
		yandexShopPasswordField.setValue(vendor.getYandexShopPassword());
		
		Button saveButton = new Button(appConstants.save(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						fetch(vendor);
						send(new UpdateVendorMessage(vendor));
					}
				});
		formPanel.addButton(saveButton);

		new FormButtonBinding(formPanel).addButton(saveButton);

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

	private void addFields(FormPanel formPanel, boolean create) {
		nameField = new TextField<String>();
		nameField.setFieldLabel(constants.nameField());
		nameField.setAllowBlank(false);
		formPanel.add(nameField);

		typeField = new SelectField<Integer>();
		typeField.setFieldLabel(constants.typeField());
		typeField.setAllowBlank(false);
		for (int type : VendorType.values.keySet()) {
			typeField.add(type, VendorType.values.get(type));
		}
		formPanel.add(typeField);

		printerField = new BooleanField(constants.printerField(), formPanel);

		customerIdField = new TextField<String>();
		customerIdField.setFieldLabel(constants.customerIdField());
		formPanel.add(customerIdField);

		keyField = new TextField<String>();
		keyField.setFieldLabel(constants.keyField());
		keyField.setAllowBlank(false);
		formPanel.add(keyField);
		
		orderImportPasswdField = new TextField<String>();
		orderImportPasswdField.setPassword(true);
		orderImportPasswdField.setFieldLabel(constants.orderImportPasswdField());
		if (!create) { 
			orderImportPasswdField.addPlugin(new HintPlugin(constants.blankPassword()));
		}	
		orderImportPasswdField.setMinLength(6);
		formPanel.add(orderImportPasswdField);

		companyNameField = new TextField<String>();
		companyNameField.setFieldLabel(constants.companyNameField());
		companyNameField.setAllowBlank(false);
		formPanel.add(companyNameField);

		emailField = new EmailField();
		emailField.setFieldLabel(constants.emailField());
		emailField.setAllowBlank(false);
		formPanel.add(emailField);

		phoneField = new TextField<String>();
		phoneField.setFieldLabel(constants.phoneField());
		formPanel.add(phoneField);

		siteField = new TextField<String>();
		siteField.setFieldLabel(constants.siteField());
		siteField.setAllowBlank(false);
		formPanel.add(siteField);

		colorField = new TextField<String>();
		colorField.setFieldLabel(constants.colorField());
		colorField.setAllowBlank(false);
		formPanel.add(colorField);

		adminEmailField = new EmailField();
		adminEmailField.setFieldLabel(constants.adminEmailField());
		adminEmailField.setAllowBlank(false);
		formPanel.add(adminEmailField);

		LabelField billData = new LabelField(constants.billData());
        billData.setStyleAttribute("font-weight", "bold");
		billData.setHideLabel(true);
		formPanel.add(billData);

		receiverField = new TextField<String>();
		receiverField.setFieldLabel(constants.receiverField());
		receiverField.setAllowBlank(false);
		formPanel.add(receiverField);

		innField = new TextField<String>();
		innField.setFieldLabel(constants.innField());
		innField.setAllowBlank(false);
		formPanel.add(innField);

		kppField = new TextField<String>();
		kppField.setFieldLabel(constants.kppField());
		formPanel.add(kppField);

		accountField = new TextField<String>();
		accountField.setFieldLabel(constants.accountField());
		accountField.setAllowBlank(false);
		formPanel.add(accountField);

		corrAccountField = new TextField<String>();
		corrAccountField.setFieldLabel(constants.corrAccountField());
		corrAccountField.setAllowBlank(false);
		formPanel.add(corrAccountField);

		bankField = new TextField<String>();
		bankField.setFieldLabel(constants.bankField());
		bankField.setAllowBlank(false);
		formPanel.add(bankField);

		bikField = new TextField<String>();
		bikField.setFieldLabel(constants.bikField());
		bikField.setAllowBlank(false);
		formPanel.add(bikField);

		LabelField roboData = new LabelField(constants.roboData());
		roboData.setHideLabel(true);
        roboData.setStyleAttribute("font-weight", "bold");
		formPanel.add(roboData);

		roboLoginField = new TextField<String>();
		roboLoginField.setFieldLabel(constants.roboLoginField());
		formPanel.add(roboLoginField);

		roboPassword1Field = new TextField<String>();
		roboPassword1Field.setFieldLabel(constants.roboPassword1Field());
		formPanel.add(roboPassword1Field);

		roboPassword2Field = new TextField<String>();
		roboPassword2Field.setFieldLabel(constants.roboPassword2Field());
		formPanel.add(roboPassword2Field);

        LabelField yandexKassaData = new LabelField(constants.yandexKassaData());
        yandexKassaData.setHideLabel(true);
        yandexKassaData.setStyleAttribute("font-weight", "bold");
        formPanel.add(yandexKassaData);

        yandexShopIdField = new NumberField();
        yandexShopIdField.setFieldLabel(constants.yandexShopIdField());
        yandexShopIdField.setPropertyEditorType(Long.class);
        formPanel.add(yandexShopIdField);

        yandexScidField = new NumberField();
        yandexScidField.setFieldLabel(constants.yandexScidField());
        yandexScidField.setPropertyEditorType(Long.class);
        formPanel.add(yandexScidField);

		yandexShopPasswordField = new TextField<String>();
		yandexShopPasswordField.setFieldLabel(constants.yandexShopPasswordField());
		formPanel.add(yandexShopPasswordField);

		LabelField smsData = new LabelField(constants.smsData());
		smsData.setHideLabel(true);
        smsData.setStyleAttribute("font-weight", "bold");
		formPanel.add(smsData);

		smsFromField = new TextField<String>();
		smsFromField.setFieldLabel(constants.smsFromField());
		formPanel.add(smsFromField);
	}

	private void fetch(Vendor vendor) {
		vendor.setName(nameField.getValue());
		vendor.setType(typeField.getXValue());
		vendor.setPrinter(printerField.getValue());
		vendor.setCustomerId(customerIdField.getValue());
		vendor.setKey(keyField.getValue());
		String password = orderImportPasswdField.getValue();
		if (password != null) {
			password = password.trim();
		}
		vendor.setOrderImportPasswd(password);
		vendor.setCompanyName(companyNameField.getValue());
		vendor.setEmail(emailField.getValue());
		vendor.setPhone(phoneField.getValue());
		vendor.setSite(siteField.getValue());
		vendor.setColor(colorField.getValue());
		vendor.setAdminEmail(adminEmailField.getValue());
		vendor.setReceiver(receiverField.getValue());
		vendor.setInn(innField.getValue());
		vendor.setKpp(kppField.getValue());
		vendor.setAccount(accountField.getValue());
		vendor.setCorrAccount(corrAccountField.getValue());
		vendor.setBank(bankField.getValue());
		vendor.setBik(bikField.getValue());
		vendor.setRoboLogin(roboLoginField.getValue());
		vendor.setRoboPassword1(roboPassword1Field.getValue());
		vendor.setRoboPassword2(roboPassword2Field.getValue());
		vendor.setSmsFrom(smsFromField.getValue());
        vendor.setYandexShopId((Long) yandexShopIdField.getValue());
        vendor.setYandexScid((Long) yandexScidField.getValue());
		vendor.setYandexShopPassword(yandexShopPasswordField.getValue());
	}

	@Override
	public void hideEditForm() {
		editWindow.hide();
	}
}
