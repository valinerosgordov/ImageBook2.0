package ru.imagebook.server.service2.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.CountryService;
import ru.imagebook.server.repository2.admin.CountryRepository;
import ru.imagebook.server.tools.CountryUtility;
import ru.imagebook.shared.model.Country;
import ru.minogin.core.server.hibernate.Dehibernate;

public class CountryServiceImpl implements CountryService{
	private final CountryRepository repository;

	@Autowired
	public CountryServiceImpl(CountryRepository repository){
		this.repository = repository;
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")	
	@Override
	@Dehibernate
	public List<Country> loadCountries() {
		return CountryUtility.PopDefaultUp(repository.loadCountries());
	}	

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void addCountry(Country country) {
		repository.saveCountry(country);		
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void updateCountry(Country modified) {
		Country country = repository.getCountry(modified.getId());
		country.setName(modified.getName());
		country.setIsDefault(modified.getIsDefault());
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void deleteCountries(List<Integer> ids) {
		repository.deleteCountries(ids);		
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public long getCountriesCount() {
		return repository.getCountriesCount();
	}
}
