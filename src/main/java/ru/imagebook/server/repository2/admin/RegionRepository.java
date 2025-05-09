package ru.imagebook.server.repository2.admin;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;

public interface RegionRepository {
	void saveRegion(Region region);

	Region getRegion(int id);

	void deleteRegions(List<Integer> ids);
	
	long getRegionsCount();
	
	List<Region> loadRegions();

	List<Region> loadRegionsForCountry(Country country);
}
