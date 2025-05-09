package ru.minogin.core.server.upload;

import java.io.File;

public interface UploadFilter {
	boolean isAllowed(File file);
}
