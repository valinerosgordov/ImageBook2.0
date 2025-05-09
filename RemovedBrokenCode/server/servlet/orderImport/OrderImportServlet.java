package ru.imagebook.server.servlet.orderImport;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ru.imagebook.server.service.OrderImportService;
import ru.imagebook.server.servlet.integration.FilterLoaderServlet;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.service.admin.order.OrderImportException;
import ru.minogin.core.client.i18n.locale.Locales;
import ru.minogin.core.server.ServiceLogger;

public class OrderImportServlet extends FilterLoaderServlet<ImportResponse> {
	private static final long serialVersionUID = 2164915018442834222L;

	private static final String IMPORT_SERVICE = "orderImportService";
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Class getObjectClass() {
		return ImportResponse.class;
	}
	
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		OrderImportService orderImportService = getBean(IMPORT_SERVICE);
		
		Map<ImportParams, String> paramsMap = new EnumMap<ImportParams, String>(
				ImportParams.class);

		InputStream body = request.getInputStream();
		PrintWriter out = response.getWriter();
		
		try {
			orderImportService.parseAndValidateXmlReq(body, paramsMap);
			Vendor vendor = orderImportService.authenticateVendor(paramsMap);
			orderImportService.loadOrder(vendor, paramsMap);	
			
			writeResponse(out, null, null);	
		} catch (OrderImportException iex) {
			writeResponse(out, iex.getErrorCode(), iex.getErrorData());	
		} catch (Exception ex) {
			writeResponse(out, ImportErrorCodes.ERR012, ex.getMessage());
			ex.printStackTrace();
		} finally {
			out.close();
		}
	}	
	
	private void writeResponse(PrintWriter out, Integer errorCode, String errorData) {
		String status = ImportResponse.STATUS_OK;
		String errCode = null;
		String errorMessage = null;		 
		
		try {
			if (errorCode != null) {
				status = ImportResponse.STATUS_ERROR;
				errCode = errorCode.toString();
				errorMessage = ImportErrorCodes.values.get(errorCode).get(Locales.RU);
			}
			
			ImportResponse response = new ImportResponse(status, errCode, errorData, errorMessage);
			
			writeResponse(out, response);
		} catch (Exception ex) {
			ServiceLogger.log(ex);
			ex.printStackTrace();
			writeEmptyResponse(out);
		}	
	} 
}