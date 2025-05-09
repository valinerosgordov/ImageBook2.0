package ru.imagebook.server.service2.pdf;

public interface PdfTransferEngine {
	void transferOrder(int orderId);

	void resetOrderState(int orderId);
}
