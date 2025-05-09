package ru.minogin.oo.server.spreadsheet;

import ru.minogin.oo.server.Doc;
import ru.minogin.oo.server.OOException;
import ru.minogin.oo.server.PathHelper;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;

public class SpreadsheetDoc extends Doc {
	private XSpreadsheetDocument doc;
	private final String encoding;

	public SpreadsheetDoc(XSpreadsheetDocument spreadsheetDocument, String encoding) {
		super(spreadsheetDocument);

		this.doc = spreadsheetDocument;
		this.encoding = encoding;
	}

	public Sheet getSheet(int n) {
		XSpreadsheets xSpreadsheets = doc.getSheets();
		try {
			String[] names = xSpreadsheets.getElementNames();
			if (n < 0 || n >= names.length)
				return null;
			Object sheet = xSpreadsheets.getByName(names[n]);
			XSpreadsheet xSpreadsheet = (XSpreadsheet) UnoRuntime.queryInterface(XSpreadsheet.class,
					sheet);
			return new Sheet(xSpreadsheet, encoding);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new OOException(e);
		}
	}

	public void saveAsExcel(String path) {
		XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, doc);
		try {
			PropertyValue[] propertyValues = new PropertyValue[1];

			propertyValues[0] = new PropertyValue();
			propertyValues[0].Name = "FilterName";
			propertyValues[0].Value = "MS Excel 97";

			storable.storeToURL(PathHelper.file(path), propertyValues);
		}
		catch (IOException e) {
			throw new OOException(e);
		}
	}
}
