package ru.imagebook.client.admin.view.country;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;

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
import com.extjs.gxt.ui.client.widget.form.CheckBox;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CountryViewImpl implements CountryView {
	@Inject
	private CountryConstants constants;
	@Inject
	private CommonConstants appConstants;

	private CountryPresenter presenter;

	private ListStore<CountryModel> store;
	private XTextField nameField;
	private CheckBox isDefaultField;
	private Window addWindow;
	private Grid<CountryModel> grid;
	private Window editWindow;

	private BasePagingLoader<PagingLoadResult<CountryModel>> loader;
	private AsyncCallback<PagingLoadResult<CountryModel>> contactsCallback;

	private RegionView regionView;
	
	@Override
	public void setPresenter(CountryPresenter presenter) {
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

		loader = new BasePagingLoader<PagingLoadResult<CountryModel>>(
				new DataProxy<PagingLoadResult<CountryModel>>() {
					@Override
					public void load(DataReader<PagingLoadResult<CountryModel>> reader,
							Object loadConfig,
							AsyncCallback<PagingLoadResult<CountryModel>> callback) {
						contactsCallback = callback;
						PagingLoadConfig config = (PagingLoadConfig) loadConfig;
						presenter.loadCountries(config.getOffset(), config.getLimit());
					}
				});

		store = new ListStore<CountryModel>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Country.NAME, constants.nameColumn(), 400));
		columns.add(new ColumnConfig(Country.IS_DEFAULT, constants.isDefaultColumn(), 200));

		grid = new Grid<CountryModel>(store, new ColumnModel(columns));

		LiveGridView liveView = new LiveGridView();
		liveView.setEmptyText(constants.emptyGrid());
		grid.setView(liveView);
		
		grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<CountryModel>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<CountryModel> se) {
				presenter.countryClicked();
			}
		});
		
		panel.add(grid);

		ToolBar bottomToolBar = new ToolBar();
		bottomToolBar.add(new FillToolItem());
		LiveToolItem item = new LiveToolItem();
		item.bindGrid(grid);
		bottomToolBar.add(item);
		panel.setBottomComponent(bottomToolBar);

		regionView = presenter.getRegionPresenter().getRegionView();
		
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FillLayout(Orientation.HORIZONTAL));
		container.add(panel);
		container.add(regionView.asWidget());
		
		return container;
	}

	@Override
	public void updateCountries() {
		loader.load();
	}

	@Override
	public void showCountries(List<Country> countries, int offset, int totalCount) {
		List<CountryModel> resultList = new ArrayList<CountryModel>();
		for (Country сountry : countries) {
			resultList.add(new CountryModel(сountry));
		}
		PagingLoadResult<CountryModel> loadResult = new BasePagingLoadResult<CountryModel>(
				resultList, offset, (int) totalCount);
		contactsCallback.onSuccess(loadResult);
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
		nameField = new XTextField(constants.nameField(), false, formPanel);
		isDefaultField = new CheckBox();
		isDefaultField.setFieldLabel(constants.isDefaultField());
		formPanel.add(isDefaultField);
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
	public boolean getIsDefault() {
		return isDefaultField.getValue();
	}

	@Override
	public void setIsDefault(boolean isDefault) {
		isDefaultField.setValue(isDefault);
	}
	
	@Override
	public Country getSelectedCountry() {
		CountryModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getCountry();
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
	public List<Country> getSelectedCountries() {
		List<Country> сountries = new ArrayList<Country>();
		List<CountryModel> items = grid.getSelectionModel().getSelectedItems();
		for (CountryModel model : items) {
			сountries.add(model.getCountry());
		}
		return сountries;
	}

	@Override
	public void alertSelectDeleteCountries() {
		MessageBox.alert(appConstants.warning(), constants.selectDeleteCountries(),
				null);
	}

	@Override
	public void alertSelectEditCountries() {
		MessageBox.alert(appConstants.warning(), constants.selectEditCountries(),
				null);
	}

	@Override
	public RegionView getRegionView() {
		return regionView;
	}
}
