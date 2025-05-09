package ru.imagebook.client.admin.ctl.vendor;

import java.util.List;

import ru.imagebook.shared.model.Vendor;

public interface VendorView {
	void showSection();

	void showAddForm();

	void showAgents(List<Vendor> agents);

	void hideAddForm();

	void showEditForm(Vendor agent);

	void hideEditForm();
}
