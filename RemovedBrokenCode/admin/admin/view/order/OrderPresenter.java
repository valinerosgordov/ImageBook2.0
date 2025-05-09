package ru.imagebook.client.admin.view.order;

public interface OrderPresenter {
	void exportButtonClicked();
	
	void bulkEditButtonClicked();
	
	void saveButtonClickedOnBulkEditForm();

	void onPublishOrderButtonClicked(int orderId);
}
