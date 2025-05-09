package ru.imagebook.server.service.pdf;

import ru.imagebook.shared.model.Order;

public interface PdfService {
    void startAsync();

    void generatePdfManually(Order<?> order);
	
	void generateTestPdf(int orderId);
}