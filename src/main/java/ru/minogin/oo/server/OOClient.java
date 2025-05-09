package ru.minogin.oo.server;

import java.io.File;

import ru.minogin.oo.server.spreadsheet.SpreadsheetDoc;
import ru.minogin.oo.server.text.TextDoc;
import ru.minogin.oo.shared.IllegalPathException;
import ru.minogin.util.shared.exceptions.Exceptions;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

public class OOClient {
	private Object desktop;

	// TODO disconnect on exit
	// TODO maybe it is better to connect on demand and not use persistent
	// connection? Or use some connection pool?
	public void connect() {
		try {
			XComponentContext xLocalContext = Bootstrap
					.createInitialComponentContext(null);
			if (xLocalContext == null)
				throw new OOException("Cannot bootstrap default open office.");
			else {
				XMultiComponentFactory xLocalServiceManager = xLocalContext
						.getServiceManager();
				if (xLocalServiceManager == null)
					throw new OOException(
							"Remote ServiceManager for open office is unavailable.");
				else {
					Object urlResolver = xLocalServiceManager.createInstanceWithContext(
							"com.sun.star.bridge.UnoUrlResolver", xLocalContext);
					XUnoUrlResolver xUrlResolver = (XUnoUrlResolver) UnoRuntime
							.queryInterface(XUnoUrlResolver.class, urlResolver);

					Object initialObject = xUrlResolver
							.resolve("uno:socket,host=localhost,port=8100;urp;StarOffice.ServiceManager");
					XPropertySet xPropertySet = (XPropertySet) UnoRuntime.queryInterface(
							XPropertySet.class, initialObject);

					Object context = xPropertySet.getPropertyValue("DefaultContext");
					XComponentContext xRemoteContext = (XComponentContext) UnoRuntime
							.queryInterface(XComponentContext.class, context);

					XMultiComponentFactory xRemoteServiceManager = xRemoteContext
							.getServiceManager();

					desktop = xRemoteServiceManager.createInstanceWithContext(
							"com.sun.star.frame.Desktop", null);
				}
			}
		}
		catch (Exception e) {
			throw new OOException("Cannot connect to open office server: ", e);
		}
	}

	public SpreadsheetDoc openSpreadsheetDoc(String path, String encoding) {
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);

		try {
			PropertyValue[] args = new PropertyValue[2];

			args[0] = new PropertyValue();
			args[0].Name = "Hidden";
			args[0].Value = true;

			XComponent xSpreadsheetComponent = xComponentLoader.loadComponentFromURL(
					PathHelper.file(path), "_blank", 0, args);
			return new SpreadsheetDoc(
					(XSpreadsheetDocument) UnoRuntime.queryInterface(
							XSpreadsheetDocument.class, xSpreadsheetComponent), encoding);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public SpreadsheetDoc createSpreadsheetDoc() {
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);

		try {
			PropertyValue[] args = new PropertyValue[2];
			args[0] = new PropertyValue();
			args[0].Name = "Hidden";
			args[0].Value = true;

			XComponent xSpreadsheetComponent = xComponentLoader.loadComponentFromURL(
					"private:factory/scalc", "_blank", 0, args);
			return new SpreadsheetDoc(
					(XSpreadsheetDocument) UnoRuntime.queryInterface(
							XSpreadsheetDocument.class, xSpreadsheetComponent), Encoding.UTF8);
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public TextDoc createTextDoc() {
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);

		try {
			PropertyValue[] args = new PropertyValue[2];
			args[0] = new PropertyValue();
			args[0].Name = "Hidden";
			args[0].Value = true;

			XComponent xTextComponent = xComponentLoader.loadComponentFromURL(
					"private:factory/swriter", "_blank", 0, args);
			return new TextDoc((XTextDocument) UnoRuntime.queryInterface(
					XTextDocument.class, xTextComponent));
		}
		catch (Exception e) {
			throw new OOException(e);
		}
	}

	public TextDoc openTextDoc(String path) {
		XComponentLoader xComponentLoader = (XComponentLoader) UnoRuntime
				.queryInterface(XComponentLoader.class, desktop);

		PropertyValue[] args = new PropertyValue[2];

		args[0] = new PropertyValue();
		args[0].Name = "Hidden";
		args[0].Value = true;

		XComponent xTextComponent;
		try {
			xTextComponent = xComponentLoader.loadComponentFromURL(
					PathHelper.file(path), "_blank", 0, args);
		}
		catch (IllegalArgumentException e) {
			throw new IllegalPathException(new File(path).getName());
		}
		catch (IOException e) {
			return Exceptions.rethrow(e);
		}
		return new TextDoc((XTextDocument) UnoRuntime.queryInterface(
				XTextDocument.class, xTextComponent));
	}
}
