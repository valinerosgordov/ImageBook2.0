package ru.minogin.oo.server;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.util.*;

public abstract class Doc {
	private Object doc;

	public Doc(Object doc) {
		this.doc = doc;
	}
	
	public Object getDoc() {
		return doc;
	}

	public void replace(String search, String replace) {
		XReplaceable replaceable = (XReplaceable) UnoRuntime.queryInterface(XReplaceable.class, doc);
		XReplaceDescriptor descriptor = replaceable.createReplaceDescriptor();
		descriptor.setSearchString(search);
		descriptor.setReplaceString(replace);
		replaceable.replaceAll(descriptor);
	}

	public void close() {
		try {
			XCloseable closeable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, doc);
			if (closeable != null)
				closeable.close(true);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			throw new OOException(e);
		}
	}

	public void save(String path) {
		XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, doc);
		try {
			storable.storeToURL(PathHelper.file(path), new PropertyValue[0]);
		}
		catch (IOException e) {
			throw new OOException(e);
		}
	}

	public void saveAsPdf(String path) {
		XStorable storable = (XStorable) UnoRuntime.queryInterface(XStorable.class, doc);
		try {
			PropertyValue[] propertyValues = new PropertyValue[1];

			propertyValues[0] = new PropertyValue();
			propertyValues[0].Name = "FilterName";
			propertyValues[0].Value = "writer_pdf_Export";

			storable.storeToURL(PathHelper.file(path), propertyValues);
		}
		catch (IOException e) {
			throw new OOException(e);
		}
	}
}
