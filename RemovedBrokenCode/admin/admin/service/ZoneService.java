package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Zone;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("zone.remoteService")
public interface ZoneService extends RemoteService{
	void addZone(Zone zone);

	void updateZone(Zone zone);

	void deleteZones(List<Integer> ids);

	List<Zone> loadZones(int offset, int limit);
	
	long getZonesCount();
	
	List<Country> loadCountries();
}
