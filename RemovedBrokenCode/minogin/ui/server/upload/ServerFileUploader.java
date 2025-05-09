package ru.minogin.ui.server.upload;

import org.apache.james.mime4j.codec.Base64InputStream;
import ru.minogin.util.shared.exceptions.Exceptions;

import java.io.*;
import java.nio.charset.Charset;

public class ServerFileUploader {
	/** File name may be obtained from servlet request parameter "filename":<br/>
	 * @RequestHeader("filename") String filename
	 * 
	 * @param inputStream - servlet input stream
	 * @param path - full path to save the uploaded file including file name */
	public void upload(InputStream inputStream, String path) {
		if (path == null)
			throw new NullPointerException();

		File file = new File(path); // TODO do not rewrite files with the same name

		Base64InputStream decodingStream = new Base64InputStream(inputStream);

		try {
			Writer w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), Charset.forName("ISO-8859-1")));

			int c;
			while ((c = decodingStream.read()) != -1) {
				w.write(c);
			}
			w.flush();
			w.close();
			decodingStream.close();
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
