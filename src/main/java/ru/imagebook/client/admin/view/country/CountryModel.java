package ru.imagebook.client.admin.view.country;

import ru.imagebook.shared.model.Country;

import com.extjs.gxt.ui.client.data.BaseModel;

public class CountryModel extends BaseModel{
	private static final long serialVersionUID = -4392020349419447389L;
	
	private final Country country;
	
	public CountryModel(Country country){
		this.country = country;
		set(Country.NAME, country.getName());
		set(Country.IS_DEFAULT, country.getIsDefault() ? "v" : "");
	}
	
	public Country getCountry(){
		return country;
	}
}
