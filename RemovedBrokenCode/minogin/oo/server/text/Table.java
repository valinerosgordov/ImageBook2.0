package ru.minogin.oo.server.text;

import java.util.Map;
import java.util.UUID;

import ru.minogin.oo.server.OOException;
import ru.minogin.oo.server.PathHelper;
import ru.minogin.oo.server.format.Format;
import ru.minogin.oo.server.format.XFormat;

import com.sun.star.awt.FontWeight;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.style.ParagraphAdjust;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XTableColumns;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTableCursor;
import com.sun.star.uno.AnyConverter;
import com.sun.star.uno.UnoRuntime;

public class Table {
	private XTextTable table;
	private XTextDocument doc;

	public Table(XTextTable table) {
		this.table = table;
	}

	public Table(XTextTable table, XTextDocument doc) {
		this(table);
		
		this.doc = doc;
	}

	public int getRowsCount() {
		XTableRows rows = table.getRows();
		return rows.getCount();
	}

	public void insertRows(int index, int count) {
		XTableRows rows = table.getRows();
		rows.insertByIndex(index, count);
	}

	public void addRows(int count) {
		insertRows(getRowsCount(), count);
	}

	public void addRow() {
		addRows(1);
	}

	public int getColumnsCount() {
		XTableColumns columns = table.getColumns();
		return columns.getCount();
	}

	public String get(int row, int column) {
		XText text = getText(row, column);
		return text.getString();
	}

	public void set(int row, int column, String value) {
		XText text = getText(row, column);
		text.setString(value);
	}

	private XText getText(int row, int column) {
		try {
			XCell cell = getCell(row, column);
			XText text = (XText) UnoRuntime.queryInterface(XText.class, cell);
			return text;
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	private XCell getCell(int row, int column) {
		try {
			XCellRange range = (XCellRange) UnoRuntime.queryInterface(XCellRange.class, table);
			XCell cell = range.getCellByPosition(column, row);
			return cell;
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public Format getFormat(int row, int column) {
		try {
			XText text = getText(row, column);
			XTextCursor cursor = text.createTextCursor();
			cursor.gotoStart(false);
			cursor.gotoEnd(true);
			XPropertySet properties = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, cursor);
			Format format = new Format();
			format.setFontName((String) properties.getPropertyValue(XFormat.CHAR_FONT_NAME));
			format.setHeight((Float) properties.getPropertyValue(XFormat.CHAR_HEIGHT));
			Float weight = (Float) properties.getPropertyValue(XFormat.CHAR_WEIGHT);
			format.setBold(weight.equals(FontWeight.BOLD));
			format.setBackgroundColor((Integer) properties.getPropertyValue(XFormat.CHAR_BACK_COLOR));

			Short paragraphAdjust = (Short) properties.getPropertyValue(XFormat.PARAGRAPH_ADJUST);
			format.setAlign(TextAlign.byStyle(paragraphAdjust.intValue()));

			return format;
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void setFormat(int row, int column, Format format) {
		try {
			XText text = getText(row, column);
			XTextCursor cursor = text.createTextCursor();
			cursor.gotoStart(false);
			cursor.gotoEnd(true);
			XPropertySet properties = (XPropertySet) UnoRuntime
					.queryInterface(XPropertySet.class, cursor);
			if (format.getFontName() != null)
				properties.setPropertyValue(XFormat.CHAR_FONT_NAME, format.getFontName());
			if (format.getHeight() != null)
				properties.setPropertyValue(XFormat.CHAR_HEIGHT, format.getHeight());
			if (format.isBold())
				properties.setPropertyValue(XFormat.CHAR_WEIGHT, FontWeight.BOLD);
			if (format.getBackgroundColor() != null)
				properties.setPropertyValue(XFormat.CHAR_BACK_COLOR, format.getBackgroundColor());
			if (format.getAlign() != null) {
				ParagraphAdjust adjust = ParagraphAdjust.fromInt(format.getAlign().getStyle());
				properties.setPropertyValue(XFormat.PARAGRAPH_ADJUST, adjust);
			}
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void setAutoHeight() {
		try {
			XTableRows rows = table.getRows();
			for (int i = 0; i < rows.getCount(); i++) {
				Object row = rows.getByIndex(i);
				XPropertySet properties = UnoRuntime.queryInterface(XPropertySet.class, row);
				properties.setPropertyValue("IsAutoHeight", true);
			}
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void setBackgroundColor(int row, int column, int color) {
		try {
			XCell cell = getCell(row, column);
			XPropertySet properties = UnoRuntime.queryInterface(XPropertySet.class, cell);
			properties.setPropertyValue(XFormat.BACK_COLOR, color);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	private String intToLiteral(int column) {
		if (column >= 0 && column < 26) {
			return String.format("%c", 'A' + column);
		}
		throw new UnsupportedOperationException();
	}

	private String cellToLiteral(int row, int column) {
		return intToLiteral(column) + Integer.valueOf(row + 1).toString();
	}

	public void mergeCells(int startRow, int endRow, int startColumn, int endColumn) {
		XTextTableCursor cursor = table.createCursorByCellName(cellToLiteral(startRow, startColumn));
		cursor.gotoCellByName(cellToLiteral(endRow, endColumn), true);
		cursor.mergeRange();
	}

	public void setCellBorders(int row, int column, Map<BorderSide, BorderLine> borders) {
		try {
			XCell cell = getCell(row, column);
			XPropertySet properties = UnoRuntime.queryInterface(XPropertySet.class, cell);

			for (BorderSide side : borders.keySet()) {
				String propertyName = side.getPropertyName();

				BorderLine line = borders.get(side);
				com.sun.star.table.BorderLine borderLine = new com.sun.star.table.BorderLine(line.getArgbColor(),
						line.getInnerLineWidth(), line.getOuterLineWidth(), line.getLineDistance());

				properties.setPropertyValue(propertyName, borderLine);
			}
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void insertImage(int row, int column, String path, int width, int height) {
		XMultiServiceFactory xMSF = UnoRuntime.queryInterface(XMultiServiceFactory.class, doc);
		XText xText = getText(row, column);

		try {
			XNameContainer xBitmapContainer = (XNameContainer) UnoRuntime.queryInterface(
					XNameContainer.class,
					xMSF.createInstance("com.sun.star.drawing.BitmapTable"));
			XTextContent xImage = (XTextContent) UnoRuntime.queryInterface(
					XTextContent.class,
					xMSF.createInstance("com.sun.star.text.TextGraphicObject"));
			XPropertySet xProps = (XPropertySet) UnoRuntime.queryInterface(
					XPropertySet.class, xImage);

			String someID = UUID.randomUUID().toString();
			xBitmapContainer.insertByName(someID, PathHelper.file(path));
			String internalURL = AnyConverter.toString(xBitmapContainer
					.getByName(someID));

			xProps.setPropertyValue("AnchorType",
					com.sun.star.text.TextContentAnchorType.AS_CHARACTER);
			xProps.setPropertyValue("GraphicURL", internalURL);
			xProps.setPropertyValue("Width", width);
			xProps.setPropertyValue("Height", height);

			xText.insertTextContent(xText.createTextCursor(), xImage, false);

			xBitmapContainer.removeByName(someID);
		} catch (Exception e) {
			throw new OOException(e);
		}
	}

}
