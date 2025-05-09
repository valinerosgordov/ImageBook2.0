package ru.imagebook.client.admin.ctl.country;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.client.admin.service.CountryServiceAsync;
import ru.imagebook.client.admin.service.RegionServiceAsync;
import ru.imagebook.client.admin.view.country.CountryPresenter;
import ru.imagebook.client.admin.view.country.CountryView;
import ru.imagebook.client.admin.view.country.RegionPresenter;
import ru.imagebook.shared.model.Country;
import ru.minogin.core.client.app.failure.XAsyncCallback;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CountryActivity extends AbstractActivity implements
		CountryPresenter {
	@Inject
	private CountryView view;
	@Inject
	private CountryServiceAsync service;

	private RegionPresenter region_controller;

	private Country country;

	@Inject
	public CountryActivity(CountryView view, RegionServiceAsync aux_service) {
		view.setPresenter(this);
		this.view = view;
		this.region_controller = new RegionController(aux_service);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

	private void getCountries(final int offset, int limit, final int total) {
		service.loadCountries(new XAsyncCallback<List<Country>>(
				) {
			@Override
			public void onSuccess(List<Country> countries) {
				view.showCountries(countries, offset, total);
			}
		});
	}

	@Override
	public void addButtonClicked() {
		view.showAddForm();
	}

	@Override
	public void saveButtonClickedOnAddForm() {
		Country country = new Country();
		country.setName(view.getName());
		country.setIsDefault(view.getIsDefault());
		service.addCountry(country, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideAddForm();
				view.updateCountries();
			}
		});
	}

	@Override
	public void editButtonClicked() {
		if (view.getSelectedCountries().isEmpty()) {
			view.alertSelectEditCountries();
		}
		else {
			country = view.getSelectedCountry();
			view.showEditForm();
			view.setName(country.getName());
			view.setIsDefault(country.getIsDefault());
		}
	}

	@Override
	public void deleteButtonClicked() {
		if (view.getSelectedCountries().isEmpty())
			view.alertSelectDeleteCountries();
		else
			view.confirmDelete();
	}

	@Override
	public void saveButtonClickedOnEditForm() {
		country.setName(view.getName());
		country.setIsDefault(view.getIsDefault());
		service.updateCountry(country, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideEditForm();
				view.updateCountries();
			}
		});
	}

	@Override
	public void deleteConfirmed() {
		List<Country> countries = view.getSelectedCountries();
		List<Integer> ids = new ArrayList<Integer>();
		for (Country country : countries) {
			ids.add(country.getId());
		}
		service.deleteCountries(ids, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.updateCountries();
				view.getRegionView().setCountry(null);
			}
		});
	}

	@Override
	public void loadCountries(final int offset, final int limit) {
		service.getCountriesCount(new XAsyncCallback<Long>() {
			@Override
			public void onSuccess(Long total) {
				long totalCount = (long) total;
				getCountries(offset, limit, (int) totalCount);
			}
		});
	}

	@Override
	public void countryClicked() {
		if (!view.getSelectedCountries().isEmpty() && view.getSelectedCountries().size() == 1) {
			region_controller.loadRegionsForCountry(view.getSelectedCountry());
		}
	}

	@Override
	public RegionPresenter getRegionPresenter() {
		return region_controller;
	}
}
