package ru.imagebook.server.service;

import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ru.imagebook.client.admin2.service.CodesImportRemoteService;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.Order;
import ru.minogin.core.client.exception.Exceptions;
import ru.minogin.core.server.spring.SpringUtil;

public class CodesImportServiceImpl implements CodesImportService, CodesImportRemoteService {
	
	private static final int BILL_ID_XLS_CELL_IDX = 0;
	private static final int BARCODE_XLS_CELL_IDX = 6;
	
	private static final String IMPORT_ERROR_SESSION_PARAM_CODE = "codesImportError";
	
	@Autowired
	private BillService billService;
	
	@Transactional
	@Override
	public void uploadBarcodes(InputStream is) {
		try {
			Workbook book = WorkbookFactory.create(is);
			Sheet sheet = book.getSheetAt(0);
			
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				
				// skip first row with headers
				int rowNum = row.getRowNum() + 1;
				if (rowNum == 1) {
					continue;
				}
				
				Cell billIdCell = row.getCell(BILL_ID_XLS_CELL_IDX);
				Cell barcodeCell = row.getCell(BARCODE_XLS_CELL_IDX);
				
				String billId = getCellStringValue(billIdCell);
				String barcode = getCellStringValue(barcodeCell);
				
				// validate
				if (StringUtils.isEmpty(billId)) {
					throw new RuntimeException(
						String.format("Ошибка в строке %s. Не указан номер счета", rowNum));
				} else if (StringUtils.isEmpty(barcode)) {
					throw new RuntimeException(
						String.format("Ошибка в строке %s. Не указан Баркод", rowNum)); 
				}
				
				// find bill and set delivery
				int billIdAsInteger = new Float(billId).intValue();
				Bill bill = billService.getBill(billIdAsInteger);
				if (bill == null) {
					throw new RuntimeException(
						String.format("Ошибка в строке %s. В БД не найден счет с Ид=%s", rowNum, billIdAsInteger));
				}
				
				barcode = barcode.replaceAll("\\s", ""); // replace white spaces 
				for (Order<?> order : bill.getOrders()) {
					order.setDeliveryCode(barcode);
				}	
			}
		} catch (Exception ex) {
			putImportErrorMessageInSession(ex);
			Exceptions.rethrow(ex);
		}
	}
	
	private String getCellStringValue(Cell cell) {
		String value = null;
		
		if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
			value = String.valueOf(cell.getNumericCellValue());
		} else {
			value = cell.getStringCellValue().trim();
		}		
		return value;
	}
	
	private void putImportErrorMessageInSession(Throwable th) {
		HttpSession session = SpringUtil.getSession();
		session.setAttribute(IMPORT_ERROR_SESSION_PARAM_CODE, th.getMessage());
	}

	@Override
	public String getImportErrorMessage() {
		HttpSession session = SpringUtil.getSession();
		return (String) session.getAttribute(IMPORT_ERROR_SESSION_PARAM_CODE);
	}
}
