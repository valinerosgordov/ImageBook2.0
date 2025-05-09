package ru.imagebook.server.repository2.admin;

import java.util.List;

import ru.imagebook.shared.model.Country;;

public interface CountryRepository {
	void saveCountry(Country country);

	Country getCountry(int id);

	void deleteCountries(List<Integer> ids);
	
	List<Country> loadCountries(int offset, int limit);
	
	long getCountriesCount();
	
	List<Country> loadCountries();
	
	Country getCountryByName(String name);
}
