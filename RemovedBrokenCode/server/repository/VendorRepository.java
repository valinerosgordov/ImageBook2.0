package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.Vendor;

public interface VendorRepository {
	List<Vendor> loadVendors();

	void saveVendor(Vendor agent);

	void updateVendor(Vendor agent);

	Vendor getVendorByCustomerId(String customerId);

	Vendor getVendorByType(int type);

	Vendor getVendorBySite(String site);

	Vendor getVendorById(Integer id);
	
	Vendor getVendorByKey(String key); 
}
