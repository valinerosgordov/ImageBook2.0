package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Zone;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ZoneServiceAsync {
	void addZone(Zone zone, AsyncCallback<Void> callback);

	void updateZone(Zone zone, AsyncCallback<Void> callback);

	void deleteZones(List<Integer> ids, AsyncCallback<Void> callback);

	void loadZones(int offset, int limit, AsyncCallback<List<Zone>> callback);

	void getZonesCount(AsyncCallback<Long> callback);

	void loadCountries(AsyncCallback<List<Country>> callback);
}
