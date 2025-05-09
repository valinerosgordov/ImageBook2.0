package ru.imagebook.client.admin.ctl.country;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.RegionServiceAsync;
import ru.imagebook.client.admin.view.country.RegionPresenter;
import ru.imagebook.client.admin.view.country.RegionView;
import ru.imagebook.client.admin.view.country.RegionViewImpl;
import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.minogin.core.client.app.failure.XAsyncCallback;

public class RegionController implements RegionPresenter {
	private RegionView view;
	private RegionServiceAsync service;
	private Region region;
	
	public RegionController(RegionServiceAsync service) {
		view = new RegionViewImpl();
		view.setPresenter(this);
		this.service = service;
	}

	@Override
	public void loadRegionsForCountry(Country country) {
		view.setCountry(country);
		service.loadRegionsForCountry(country, new XAsyncCallback<List<Region>>() {
			@Override
			public void onSuccess(List<Region> result) {
				view.showRegions(result);
			}
		});
	}

	@Override
	public RegionView getRegionView() {
		return view;
	}

	@Override
	public void addButtonClicked() {
		view.showAddForm();
	}

	@Override
	public void editButtonClicked() {
		if (view.getSelectedRegions().isEmpty()) {
			view.alertSelectEditRegions();
		} else {
			region = view.getSelectedRegion();
			view.showEditForm(region);
		}
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedRegions().isEmpty())
			view.alertSelectDeleteRegions();
		else
			view.confirmDelete();
	}

	@Override
	public void deleteConfirmed() {
		List<Region> regions = view.getSelectedRegions();
		List<Integer> ids = new ArrayList<Integer>();
		for (Region region : regions) {
			ids.add(region.getId());
		}
		service.deleteRegions(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.updateRegions();
			}
		});
	}

	@Override
	public void saveButtonClickedOnEditForm() {
		int regionId = region.getId();
		region = view.getRegionProperties();
		region.setId(regionId);
		service.updateRegion(region, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				view.updateRegions();
			}
		});
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		Region region = new Region();
		region = view.getRegionProperties();
		service.addRegion(region, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				view.updateRegions();
			}
		});
	}
}
