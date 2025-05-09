package ru.imagebook.server.service2.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin.service.RegionService;
import ru.imagebook.server.repository2.admin.RegionRepository;
import ru.imagebook.shared.model.Country;
import ru.imagebook.shared.model.Region;
import ru.minogin.core.server.hibernate.Dehibernate;

public class RegionServiceImpl implements RegionService {
	private final RegionRepository repository;

	@Autowired
	public RegionServiceImpl(RegionRepository repository){
		this.repository = repository;
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void addRegion(Region region) {
		repository.saveRegion(region);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void updateRegion(Region modified) {
		Region region = repository.getRegion(modified.getId());
		region.setName(modified.getName());
		region.setCountry(modified.getCountry());
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public void deleteRegions(List<Integer> ids) {
		repository.deleteRegions(ids);
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")	
	@Override
	@Dehibernate
	public List<Region> loadRegions() {
		return repository.loadRegions();
	}

	@Transactional
	@PreAuthorize("hasRole('ADMIN')")	
	@Override
	@Dehibernate
	public List<Region> loadRegionsForCountry(Country country) {
		return repository.loadRegionsForCountry(country);
	}
	
	@Transactional
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public long getRegionsCount() {
		return repository.getRegionsCount();
	}

}
