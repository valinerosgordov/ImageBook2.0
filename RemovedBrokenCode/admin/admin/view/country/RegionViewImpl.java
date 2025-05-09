package ru.imagebook.client.admin.view.country;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.XContentPanel;
import ru.minogin.core.client.gxt.XWindow;
import ru.minogin.core.client.gxt.form.XFormPanel;
import ru.minogin.core.client.gxt.form.XTextField;
import ru.minogin.core.client.gxt.grid.FixedLiveGridView;

import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
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
import com.extjs.gxt.ui.client.widget.toolbar.FillToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LiveToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class RegionViewImpl implements RegionView {

	private RegionConstants constants;
	private CommonConstants appConstants;
	
	private RegionPresenter presenter;
	private BasePagingLoader<PagingLoadResult<RegionModel>> loader;
	private ListStore<RegionModel> store;
	protected AsyncCallback<PagingLoadResult<RegionModel>> contactsCallback;
	private Grid<RegionModel> grid;
	
	private Window addWindow;
	private Window editWindow;
	
	private XTextField nameField;
	private Country country;
	
	public RegionViewImpl() {
		constants = GWT.create(RegionConstants.class);
		appConstants = GWT.create(CommonConstants.class);
	}
	
	@Override
	public void setPresenter(RegionPresenter presenter) {
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

		loader = new BasePagingLoader<PagingLoadResult<RegionModel>>(
				new DataProxy<PagingLoadResult<RegionModel>>() {
					@Override
					public void load(DataReader<PagingLoadResult<RegionModel>> reader,
							Object loadConfig,
							AsyncCallback<PagingLoadResult<RegionModel>> callback) {
						contactsCallback = callback;
					}
				});

		store = new ListStore<RegionModel>(loader);

		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig(Region.NAME, constants.nameColumn(), 400));

		grid = new Grid<RegionModel>(store, new ColumnModel(columns));

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

		return panel;
	}

	@Override
	public void showRegions(List<Region> regions) {
		List<RegionModel> resultList = new ArrayList<RegionModel>();
		for (Region region : regions) {
			resultList.add(new RegionModel(region));
		}
		
		PagingLoadResult<RegionModel> loadResult = new BasePagingLoadResult<RegionModel>(resultList);
		contactsCallback.onSuccess(loadResult);
	}

	@Override
	public void updateRegions() {
		presenter.loadRegionsForCountry(country);
	}

	private void addFields(FormPanel formPanel, Region region) {
		//countryField = new SelectField<Country>(constants.countryField(), false, formPanel);
		nameField = new XTextField(constants.nameField(), false, formPanel);
		if (region != null) {
			nameField.setValue(region.getName());
		}
	}
	
	@Override
	public void showAddForm() {
		addWindow = new XWindow(constants.addFormHeading());
		addWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel, null);

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

	@Override
	public void hideAddForm() {
		addWindow.hide();
	}

	@Override
	public Region getSelectedRegion() {
		RegionModel item = grid.getSelectionModel().getSelectedItem();
		if (item == null)
			return null;
		return item.getRegion();
	}

	@Override
	public void showEditForm(Region region) {
		editWindow = new XWindow(constants.editFormHeading());
		editWindow.setHeight(150);

		FormPanel formPanel = new XFormPanel();
		addFields(formPanel, region);

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
	public List<Region> getSelectedRegions() {
		List<Region> regions = new ArrayList<Region>();
		List<RegionModel> items = grid.getSelectionModel().getSelectedItems();
		for (RegionModel model : items) {
			regions.add(model.getRegion());
		}
		return regions;
	}

	@Override
	public void alertSelectDeleteRegions() {
		MessageBox.alert(appConstants.warning(), constants.selectDeleteCountries(), null);
	}

	@Override
	public void alertSelectEditRegions() {
		MessageBox.alert(appConstants.warning(), constants.selectEditCountries(), null);
	}

	@Override
	public void setCountry(Country country) {
		this.country = country;
		if (country == null) {
			showRegions(new ArrayList<Region>());
		}
	}

	@Override
	public Region getRegionProperties() {
		Region region = new Region();
		region.setCountry(country);
		region.setName(nameField.getValue());
		return region;
	}

}
