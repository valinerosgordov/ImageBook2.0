package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("region.remoteService")
public interface RegionService extends RemoteService {
	void addRegion(Region region);

	void updateRegion(Region region);

	void deleteRegions(List<Integer> ids);

	List<Region> loadRegions();
	
	List<Region> loadRegionsForCountry(Country country);
	
	long getRegionsCount();
}
