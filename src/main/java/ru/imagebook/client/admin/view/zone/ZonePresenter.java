package ru.imagebook.client.admin.view.zone;

import ru.imagebook.shared.model.Country;

public interface ZonePresenter {
	void addButtonClicked();

	void saveButtonClickedOnAddForm();

	void editButtonClicked();

	void deleteButtonClicked();

	void saveButtonClickedOnEditForm();

	void deleteConfirmed();

	void loadZones(int offset, int limit);

	void importButtonClicked();
	
	void setCurrentCountry(Country country);
}
