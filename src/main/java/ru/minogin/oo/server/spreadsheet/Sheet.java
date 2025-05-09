package ru.minogin.oo.server.spreadsheet;

import java.util.Date;

import ru.minogin.oo.server.Encoding;
import ru.minogin.oo.server.OOException;
import ru.minogin.oo.server.format.Format;
import ru.minogin.oo.server.format.XFormat;
import ru.minogin.oo.server.util.DateUtil;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.XPropertySet;
import com.sun.star.sheet.XCellRangeAddressable;
import com.sun.star.sheet.XSheetCellCursor;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XUsedAreaCursor;
import com.sun.star.table.CellRangeAddress;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

public class Sheet {
	private XSpreadsheet sheet;
	private final String encoding;

	public Sheet(XSpreadsheet sheet, String encoding) {
		this.sheet = sheet;
		this.encoding = encoding;
	}

	public String getText(int x, int y) {
		try {
			XText xText = (XText) UnoRuntime.queryInterface(XText.class,
					sheet.getCellByPosition(x, y));

			String text = xText.getString();
			if (encoding.equals(Encoding.CP1251))
				text = new String(text.getBytes("Cp1252"), "Cp1251");

			return text;
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void setText(int x, int y, String text) {
		try {
			XText xText = (XText) UnoRuntime.queryInterface(XText.class,
					sheet.getCellByPosition(x, y));
			xText.setString(text);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void setNumber(int x, int y, double value) {
		try {
			XCell cell = sheet.getCellByPosition(x, y);
			cell.setValue(value);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public Date getDate(int x, int y) {
		return DateUtil.guessDate(getText(x, y));
	}

	public int getColumnsCount() {
		XSheetCellCursor cursor = sheet.createCursor();
		XUsedAreaCursor usedCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(
				XUsedAreaCursor.class, cursor);
		usedCursor.gotoEndOfUsedArea(true);
		XCellRangeAddressable addressable = (XCellRangeAddressable) UnoRuntime
				.queryInterface(XCellRangeAddressable.class, usedCursor);
		CellRangeAddress address = addressable.getRangeAddress();
		return address.EndColumn + 1;
	}

	public int getRowsCount() {
		XSheetCellCursor cursor = sheet.createCursor();
		XUsedAreaCursor usedCursor = (XUsedAreaCursor) UnoRuntime.queryInterface(
				XUsedAreaCursor.class, cursor);
		usedCursor.gotoEndOfUsedArea(true);
		XCellRangeAddressable addressable = (XCellRangeAddressable) UnoRuntime
				.queryInterface(XCellRangeAddressable.class, usedCursor);
		CellRangeAddress address = addressable.getRangeAddress();
		return address.EndRow + 1;
	}

	public void setFormat(int x, int y, Format format) {
		try {
			XPropertySet properties = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, sheet.getCellByPosition(x, y));
			if (format.getFontName() != null)
				properties.setPropertyValue(XFormat.CHAR_FONT_NAME,
						format.getFontName());
			if (format.getHeight() != null)
				properties.setPropertyValue(XFormat.CHAR_HEIGHT, format.getHeight());
			if (format.isBold())
				properties.setPropertyValue(XFormat.CHAR_WEIGHT, FontWeight.BOLD);
			if (format.getBackgroundColor() != null)
				properties.setPropertyValue(XFormat.CHAR_BACK_COLOR,
						format.getBackgroundColor());
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}
}
