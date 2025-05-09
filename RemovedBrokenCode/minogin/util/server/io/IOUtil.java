package ru.minogin.util.server.io;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import ru.minogin.util.shared.exceptions.Exceptions;

public class IOUtil {
	public static void copy(String filePath, OutputStream outputStream) {
		try {
			FileInputStream is = new FileInputStream(filePath);
			BufferedOutputStream os = new BufferedOutputStream(outputStream);
			IOUtils.copy(is, os);
			is.close();
			os.flush();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
