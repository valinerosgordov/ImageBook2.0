package ru.imagebook.client.admin.view.country;

import ru.imagebook.shared.model.Region;

import com.extjs.gxt.ui.client.data.BaseModel;

public class RegionModel extends BaseModel {
	private static final long serialVersionUID = -2637510514942420728L;

	private final Region region;
	
	public RegionModel(Region region) {
		this.region = region;
		set(Region.NAME, region.getName());
	}
	
	public Region getRegion() {
		return region;
	}
}
