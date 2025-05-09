package ru.imagebook.client.admin.service;

import java.util.List;
import ru.imagebook.shared.model.Country;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("country.remoteService")
public interface CountryService extends RemoteService{
	void addCountry(Country country);

	void updateCountry(Country country);

	void deleteCountries(List<Integer> ids);

	List<Country> loadCountries();
	
	long getCountriesCount();
}
