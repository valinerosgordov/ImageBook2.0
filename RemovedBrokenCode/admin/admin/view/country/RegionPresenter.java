package ru.imagebook.client.admin.view.country;

import ru.imagebook.shared.model.Country;

public interface RegionPresenter {
	public void loadRegionsForCountry(Country country);
	
	public RegionView getRegionView();

	public void addButtonClicked();

	public void editButtonClicked();

	public void deleteButtonClicked();

	public void deleteConfirmed();

	public void saveButtonClickedOnEditForm();

	public void saveButtonClickedOnAddForm();
}
