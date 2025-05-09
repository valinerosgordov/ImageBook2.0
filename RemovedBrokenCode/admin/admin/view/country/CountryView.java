package ru.imagebook.client.admin.view.country;

import java.util.List;

import ru.imagebook.shared.model.Country;

import com.google.gwt.user.client.ui.IsWidget;

public interface CountryView extends IsWidget {
	void updateCountries();

	void showCountries(List<Country> countries, int offset, int totalCount);

	void showAddForm();

	String getName();

	void setName(String name);

	void hideAddForm();

	Country getSelectedCountry();

	void showEditForm();

	void hideEditForm();

	void confirmDelete();

	List<Country> getSelectedCountries();

	void alertSelectDeleteCountries();

	void alertSelectEditCountries();

	void setPresenter(CountryPresenter presenter);

	boolean getIsDefault();

	void setIsDefault(boolean isDefault);
	
	RegionView getRegionView();
}
