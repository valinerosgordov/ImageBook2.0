package ru.imagebook.server.repository;

import ru.imagebook.shared.model.Vendor;

public interface SecurityRepository {
	void enableVendorFilters(Vendor vendor);
}
