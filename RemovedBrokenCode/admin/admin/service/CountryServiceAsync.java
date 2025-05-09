package ru.imagebook.client.admin.service;

import java.util.List;

import ru.imagebook.shared.model.Country;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CountryServiceAsync {
	void addCountry(Country country, AsyncCallback<Void> callback);

	void updateCountry(Country country, AsyncCallback<Void> callback);

	void deleteCountries(List<Integer> ids, AsyncCallback<Void> callback);
	
	void loadCountries(AsyncCallback<List<Country>> callback);
	
	void getCountriesCount(AsyncCallback<Long> callback);
}
