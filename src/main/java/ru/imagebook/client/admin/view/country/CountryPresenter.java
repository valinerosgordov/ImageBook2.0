package ru.imagebook.client.admin.view.country;

public interface CountryPresenter {
	void addButtonClicked();

	void saveButtonClickedOnAddForm();

	void editButtonClicked();

	void deleteButtonClicked();

	void saveButtonClickedOnEditForm();

	void deleteConfirmed();

	void loadCountries(int offset, int limit);
	
	void countryClicked();
	
	RegionPresenter getRegionPresenter();
}
