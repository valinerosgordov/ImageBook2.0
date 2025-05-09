package ru.imagebook.server.service;

import java.io.InputStream;

public interface CodesImportService {
	void uploadBarcodes(InputStream is);
}
