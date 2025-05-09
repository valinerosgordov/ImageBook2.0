package ru.imagebook.client.admin.view.country;


import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public interface RegionView extends IsWidget {

	void setPresenter(RegionPresenter presenter);
	
	public Widget asWidget();
	
	public void showRegions(List<Region> regions);
	
	void updateRegions();

	void showAddForm();

	void hideAddForm();

	Region getSelectedRegion();

	void hideEditForm();

	void confirmDelete();

	List<Region> getSelectedRegions();

	void alertSelectDeleteRegions();

	void alertSelectEditRegions();

	void setCountry(Country country);

	void showEditForm(Region region);

	Region getRegionProperties();
}
