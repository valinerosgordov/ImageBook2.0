package ru.imagebook.server.service;

import java.io.InputStream;
import java.util.Map;

import ru.imagebook.server.servlet.orderImport.ImportParams;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.service.admin.order.OrderImportException;

public interface OrderImportService {
	Vendor authenticateVendor(Map<ImportParams, String> paramsMap) throws OrderImportException;
	
	void parseAndValidateXmlReq(InputStream body, Map<ImportParams, String> paramsMap) 
			throws OrderImportException;
	
	void loadOrder(Vendor vendor, Map<ImportParams, String> paramsMap) throws OrderImportException;
}
