package ru.minogin.core.server.zip;

import java.io.File;

public interface UnpackFilter {
	boolean isAllowed(File file);
}
