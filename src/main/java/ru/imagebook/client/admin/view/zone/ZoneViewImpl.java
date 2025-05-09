package ru.imagebook.client.admin.view.zone;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.imagebook.shared.model.Zone;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextArea;
import ru.minogin.core.client.gxt.form.XTextField;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.ProgressBar;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FileUploadField;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Encoding;
import com.extjs.gxt.ui.client.widget.form.FormPanel.Method;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.LiveGridView;
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ZoneViewImpl implements ZoneView {
	@Inject
	private ZoneConstants constants;
	@Inject
	private CommonConstants appConstants;
	@Inject
	private ZoneMessages messages;

	private ZonePresenter presenter;

	private ListStore<ZoneModel> store;
	private XTextArea zipField;
	private SelectField<Country> countryField;
	private SelectField<Region> regionField;
	private XTextArea districtField;
	private XTextField cityField;
	private Window addWindow;
	private Grid<ZoneModel> grid;
	private Window editWindow;
	private Window importWindow;

	private BasePagingLoader<PagingLoadResult<ZoneModel>> loader;
	private AsyncCallback<PagingLoadResult<ZoneModel>> contactsCallback;

	private ProgressBar fileUploadingProgressBar;
	private ProgressBar fileParsingProgressBar;
	private MessageBox fileUploadingProgressBox;
	private MessageBox fileParsingProgressBox;

	@Override
	public void setPresenter(ZonePresenter presenter) {
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
		toolBar.add(new Button(constants.importButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						presenter.importButtonClicked();
					}
				}));
		panel.setTopComponent(toolBar);

		loader = new BasePagingLoader<PagingLoadResult<ZoneModel>>(
				new DataProxy<PagingLoadResult<ZoneModel>>() {
					@Override
					public void load(DataReader<PagingLoadResult<ZoneModel>> reader,
							Object loadConfig,
							AsyncCallback<PagingLoadResult<ZoneModel>> callback) {
						contactsCallback = callback;
						PagingLoadConfig config = (PagingLoadConfig) loadConfig;
						presenter.loadZones(config.getOffset(), config.getLimit());
					}
				});

		store = new ListStore<ZoneModel>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Zone.ZIP, constants.zipColumn(), 200));
		columns.add(new ColumnConfig(Zone.COUNTRY, constants.countryColumn(), 200));
		columns.add(new ColumnConfig(Zone.REGION, constants.regionColumn(), 200));
		columns
				.add(new ColumnConfig(Zone.DISTRICT, constants.districtColumn(), 200));
		columns.add(new ColumnConfig(Zone.CITY, constants.cityColumn(), 200));

		grid = new Grid<ZoneModel>(store, new ColumnModel(columns));

		LiveGridView liveView = new LiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);

		panel.add(grid);

		ToolBar bottomToolBar = new ToolBar();
		bottomToolBar.add(new FillToolItem());
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		bottomToolBar.add(item);
		panel.setBottomComponent(bottomToolBar);

		return panel;
	}

	@Override
	public void updateZones() {
		loader.load();
	}

	@Override
	public void showZones(List<Zone> zones, int offset, int totalCount) {
		List<ZoneModel> resultList = new ArrayList<ZoneModel>();
		for (Zone zone : zones) {
			resultList.add(new ZoneModel(zone));
		}
		PagingLoadResult<ZoneModel> loadResult = new BasePagingLoadResult<ZoneModel>(
				resultList, offset, (int) totalCount);
		contactsCallback.onSuccess(loadResult);
	}

	@Override
	public void showAddForm(List<Country> countries, List<Region> regions) {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setHeight(250);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel, countries, regions, null);
		
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

	private void addFields(FormPanel formPanel, List<Country> countries, List<Region> regions, Zone zone) {
		zipField = new XTextArea(constants.zipField(), true, formPanel);

		countryField = new SelectField<Country>();
		countryField.setFieldLabel(constants.countryField());
		//countryField.setId(Country.ID);
		
		for (Country country : countries) {
			countryField.add(country, country.getName());
		}

		formPanel.add(countryField);
		
		regionField = new SelectField<Region>();
		regionField.setFieldLabel(constants.regionField());
		//regionField.setId(Region.ID);
		formPanel.add(regionField);
		for (Region region : regions) {
			regionField.add(region, region.getName());
		}
		
		districtField = new XTextArea(constants.districtField(), true, formPanel);
		cityField = new XTextField(constants.cityField(), false, formPanel);
		
		if (zone == null) {
			countryField.selectFirst();
		} else {
			zipField.setValue(zone.getZip());
			countryField.setXValue(zone.getCountry());
			regionField.setXValue(zone.getRegion());
			districtField.setValue(zone.getDistrict());
			cityField.setValue(zone.getCity());
		}
		countryField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Country>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Country>> se) {
				presenter.setCurrentCountry(countryField.getXValue());
			}
		});
	}

	@Override
	public String getZip() {
		return zipField.getValue();
	}

	@Override
	public void setZip(String name) {
		zipField.setValue(name);
	}

	@Override
	public Zone getZoneProperties() {
		Zone zone = new Zone();
		zone.setZip(zipField.getValue());
		zone.setCountry(countryField.getXValue());
		zone.setRegion(regionField.getXValue());
		zone.setDistrict(districtField.getValue());
		zone.setCity(cityField.getValue());
		return zone;
	}

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public Zone getSelectedZone() {
		ZoneModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getZone();
	}

	@Override
	public void showEditForm(List<Country> countries, List<Region> regions, Zone zone) {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setHeight(300);

		FormPanel formPanel = new XFormPanel();

		addFields(formPanel, countries, regions, zone);

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
	public List<Zone> getSelectedZones() {
		List<Zone> zones = new ArrayList<Zone>();
		List<ZoneModel> items = grid.getSelectionModel().getSelectedItems();
		for (ZoneModel model : items) {
			zones.add(model.getZone());
		}
		return zones;
	}

	@Override
	public void alertSelectDeleteZones() {
		MessageBox.alert(appConstants.warning(), constants.selectDeleteZones(),
				null);
	}

	@Override
	public void alertSelectEditZones() {
		MessageBox.alert(appConstants.warning(), constants.selectEditZones(), null);
	}

	@Override
	public void showImportForm() {
		importWindow = new XWindow(constants.importFormHeading());
		importWindow.setHeight(150);

		final FormPanel formPanel = new XFormPanel();
		formPanel.setAction(GWT.getHostPageBaseURL() + "uploadZones");
		formPanel.setEncoding(Encoding.MULTIPART);
		formPanel.setMethod(Method.POST);

		FileUploadField fileUploadField = new FileUploadField();
		fileUploadField.setAllowBlank(false);
		fileUploadField.setName("uploadField");
		fileUploadField.setFieldLabel(constants.fileToImportField());
		formPanel.add(fileUploadField);

		Button btn = new Button(constants.doImportButton());
		btn.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				fileUploadingProgressBox = MessageBox.progress(
						constants.progressBoxTitle(), constants.fileUploadingMessage(),
						constants.initUploadLabel());
				fileUploadingProgressBar = fileUploadingProgressBox.getProgressBar();
				formPanel.submit();
			}
		});
		formPanel.addButton(btn);

		formPanel.addButton(new Button(appConstants.cancel(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						importWindow.hide();
					}
				}));

		formPanel.setButtonAlign(HorizontalAlignment.LEFT);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(btn);

		importWindow.add(formPanel);
		importWindow.show();
	}

	@Override
	public void closeImportForm() {
		importWindow.hide();
	}

	@Override
	public void updateFileLoadingProgressBox(double percent) {
		fileUploadingProgressBar.updateProgress(percent, (int) percent + "% "
				+ constants.completeLabel());
	}

	@Override
	public void updateFileParsingProgressBox(double percent) {
		fileParsingProgressBar.updateProgress(percent, (int) percent + "% "
				+ constants.completeLabel());
	}

	@Override
	public void closeFileLoadingProgressBox() {
		fileUploadingProgressBox.close();
		Info.display(constants.messageLabel(), constants.fileUploadingSuccess(), "");
	}

	@Override
	public void closeFileParsingProgressBox() {
		fileParsingProgressBox.close();
		Info.display(constants.messageLabel(), constants.fileParsingSuccess(), "");
	}

	@Override
	public void showFileParsingProgressBox() {
		fileParsingProgressBox = MessageBox.progress(constants.progressBoxTitle(),
				constants.fileParsingMessage(), constants.initParseLabel());
		fileParsingProgressBar = fileParsingProgressBox.getProgressBar();
	}

	@Override
	public void showParseErrorMessage(int rowIndex, int fieldIndex) {
		String errorMessage = messages.parseErrorMessage(rowIndex, fieldIndex);
		MessageBox.alert(appConstants.error(), errorMessage,
				new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent ce) {
						fileParsingProgressBox.close();
					}
				});
	}

	@Override
	public void showFileTypeErrorMessage() {
		MessageBox.alert(appConstants.error(), constants.fileTypeError(),
				new Listener<MessageBoxEvent>() {
					public void handleEvent(MessageBoxEvent ce) {
						fileParsingProgressBox.close();
					}
				});
	}

	@Override
	public void setRegions(List<Region> result) {
		regionField.clear();
		regionField.removeAll();
		for (Region region : result) {
			regionField.add(region, region.getName());
		}
	}
}
