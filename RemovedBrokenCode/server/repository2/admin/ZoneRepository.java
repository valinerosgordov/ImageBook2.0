package ru.imagebook.server.repository2.admin;

import java.util.List;
import ru.imagebook.shared.model.Zone;

public interface ZoneRepository {
	void saveZone(Zone zone);

	Zone getZone(int id);

	void deleteZones(List<Integer> ids);
	
	List<Zone> loadZones(int offset, int limit);
	
	long getZonesCount();
	
	void deleteAllZones();
}
