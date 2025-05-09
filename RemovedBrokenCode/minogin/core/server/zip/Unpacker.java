package ru.minogin.core.server.zip;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.IOUtils;

import ru.minogin.core.client.exception.Exceptions;

// TODO progress callback

public class Unpacker {
	private final File file;
	private String path;
	private boolean deleteZip = true;
	private UnpackFilter filter;

	public Unpacker(File file) {
		this.file = file;
		path = file.getParentFile().getPath();
	}

	public void setDeleteZip(boolean deleteZip) {
		this.deleteZip = deleteZip;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFilter(UnpackFilter filter) {
		this.filter = filter;
	}
	
	@SuppressWarnings("unchecked")
	public List<File> unpack() {
		try {
			List<File> files = new ArrayList<File>();

			ZipFile zipFile = new ZipFile(file, "CP866");
			Enumeration<ZipArchiveEntry> entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipArchiveEntry entry = entries.nextElement();
				String name = entry.getName();

				if (entry.isDirectory()) {
					File folder = new File(path + "/" + name);
					folder.mkdirs();
				}
				else {
					File outFile = new File(path + "/" + name);
					if (filter == null || file.isDirectory() || filter.isAllowed(outFile)) {
						InputStream is = zipFile.getInputStream(entry);
						File parentFile = outFile.getParentFile();
						if (parentFile != null)
							parentFile.mkdirs();
						FileOutputStream out = new FileOutputStream(outFile);
						BufferedOutputStream os = new BufferedOutputStream(out);
						try {
							IOUtils.copy(is, os);
						}
						finally {
							is.close();
							os.close();
						}
						files.add(outFile);
					}
				}
			}
			zipFile.close();

			if (deleteZip)
				file.delete();

			return files;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}
}
