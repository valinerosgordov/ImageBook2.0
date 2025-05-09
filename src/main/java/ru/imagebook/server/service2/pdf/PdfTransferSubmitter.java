package ru.imagebook.server.service2.pdf;

public interface PdfTransferSubmitter {
	void resetOrdersInProgress();

	void submitNewOrders();
}
