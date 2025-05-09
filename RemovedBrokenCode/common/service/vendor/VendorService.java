package ru.imagebook.client.common.service.vendor;

import ru.imagebook.client.common.service.UserService;
import ru.imagebook.shared.model.Vendor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class VendorService {
	private final UserService userService;

	private Vendor vendor;

	@Inject
	public VendorService(UserService userService) {
		this.userService = userService;
	}

	public Vendor getVendor() {
		if (vendor == null)
			return userService.getUser().getVendor();

		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
}
