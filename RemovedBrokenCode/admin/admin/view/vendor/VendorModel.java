package ru.imagebook.client.admin.view.vendor;

import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.gxt.BaseEntityModel;

public class VendorModel extends BaseEntityModel<Vendor> {
	private static final long serialVersionUID = -5899379381851713086L;

	public VendorModel(Vendor vendor) {
		super(vendor);

		set(Vendor.NAME, vendor.getName());
		set(Vendor.CUSTOMER_ID, vendor.getCustomerId());
		set(Vendor.PRINTER, vendor.isPrinter() ? "v" : "");
	}
}
