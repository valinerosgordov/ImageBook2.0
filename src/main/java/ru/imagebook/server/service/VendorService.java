package ru.imagebook.server.service;

import java.util.List;

import ru.imagebook.shared.model.Vendor;

public interface VendorService {
	List<Vendor> loadVendors();

	void saveVendor(Vendor vendor);

	void updateVendor(Vendor vendor);

	Vendor getVendorByCustomerId(String customerId);

	Vendor getMainVendor();

	Vendor getVendorByCurrentSite();

	Vendor getVendorById(Integer vendorId);
	
	Vendor authenticateVendor(String key, String password);

    Vendor getVendorByKey(String key);
}
