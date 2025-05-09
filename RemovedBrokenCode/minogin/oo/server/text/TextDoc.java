package ru.minogin.oo.server.text;

import ru.minogin.oo.server.Doc;
import ru.minogin.oo.server.OOException;
import ru.minogin.oo.server.PathHelper;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XIndexAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.text.XText;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextTable;
import com.sun.star.text.XTextTablesSupplier;
import com.sun.star.uno.UnoRuntime;

public class TextDoc extends Doc {
	private final XTextDocument doc;
	private XComponent xComponent;

	public TextDoc(XTextDocument textDocument) {
		super(textDocument);

		this.doc = textDocument;
	}
	
	public XTextDocument getDoc() {
		return doc;
	}

	public TextDoc(XTextDocument textDocument, XComponent xComponent) {
		super(textDocument);
		this.doc = textDocument;
		this.xComponent = xComponent;
	}

	public void saveAsRTF(String path) {
		XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class,
				doc);
		try {
			PropertyValue[] propertyValues = new PropertyValue[1];

			propertyValues[0] = new PropertyValue();
			propertyValues[0].Name = "FilterName";
			propertyValues[0].Value = "Rich Text Format";

			storable.storeToURL(PathHelper.file(path), propertyValues);
		}
		catch (IOException e) {
			throw new OOException(e);
		}
	}

	public void saveAsDoc(String path) {
		XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class,
				doc);
		try {
			PropertyValue[] propertyValues = new PropertyValue[1];

			propertyValues[0] = new PropertyValue();
			propertyValues[0].Name = "FilterName";
			propertyValues[0].Value = "MS Word 97";

			storable.storeToURL(PathHelper.file(path), propertyValues);
		}
		catch (IOException e) {
			throw new OOException(e);
		}
	}

	public Table getTable(int n) {
		try {
			XTextTablesSupplier tablesSupplier = (XTextTablesSupplier) UnoRuntime
					.queryInterface(XTextTablesSupplier.class, doc);
			XNameAccess namedTables = tablesSupplier.getTextTables();
			XIndexAccess tables = (XIndexAccess) UnoRuntime.queryInterface(
					XIndexAccess.class, namedTables);

			Object xTable = tables.getByIndex(n);
			XTextTable table = (XTextTable) UnoRuntime.queryInterface(
					XTextTable.class, xTable);
			return new Table(table, doc);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public void insertFile(String path) {
		try {
			XText text = doc.getText();
			XTextCursor cursor = text.createTextCursor();
			cursor.gotoEnd(false);
			XDocumentInsertable insertable = UnoRuntime.queryInterface(
					XDocumentInsertable.class, cursor);
			insertable.insertDocumentFromURL(PathHelper.file(path),
					new PropertyValue[0]);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public XComponent getXComponent() {
		return this.xComponent;
	}

}
