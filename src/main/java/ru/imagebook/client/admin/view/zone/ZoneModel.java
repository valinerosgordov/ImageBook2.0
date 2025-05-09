package ru.imagebook.client.admin.view.zone;

import java.util.List;

import ru.imagebook.shared.model.Zone;

import com.extjs.gxt.ui.client.data.BaseModel;

public class ZoneModel extends BaseModel{
	private static final long serialVersionUID = 4802199022581958300L;
	private final Zone zone;
	
	public ZoneModel(Zone zone){
		this.zone = zone;
		
		set(Zone.ZIP, zone.getZip());
		set(Zone.COUNTRY, (zone.getCountry()== null) ? "" : zone.getCountry().getName());
		set(Zone.REGION, (zone.getRegion() == null) ? "" : zone.getRegion().getName());
		
		List<String> districts = zone.getDistricts();
		StringBuilder districtString = new StringBuilder();
		boolean isFirst = true;
		for (String string : districts) {
			if (!isFirst) {
				districtString.append(", ");
			} else {
				isFirst = false;
			}
			districtString.append(string);
		}
		set(Zone.DISTRICT, districtString);
		
		set(Zone.CITY, zone.getCity());
	}
	
	public Zone getZone(){
		return zone;
	}
}
