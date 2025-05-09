package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RegionServiceAsync {
	void addRegion(Region region, AsyncCallback<Void> callback);

	void updateRegion(Region region, AsyncCallback<Void> callback);

	void deleteRegions(List<Integer> ids, AsyncCallback<Void> callback);
	
	void loadRegions(AsyncCallback<List<Region>> callback);
	
	void getRegionsCount(AsyncCallback<Long> callback);
	
	void loadRegionsForCountry(Country country, AsyncCallback<List<Region>> callback);
}
