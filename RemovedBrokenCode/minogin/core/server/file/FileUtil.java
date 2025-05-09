package ru.minogin.core.server.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import ru.minogin.core.client.exception.Exceptions;

public class FileUtil {
	public static String read(File file) {
		try {
			FileInputStream is = new FileInputStream(file);
			FileChannel channel = is.getChannel();
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			return Charset.forName("UTF-8").decode(buffer).toString();
		}
		catch (IOException e) {
			return Exceptions.rethrow(e);
		}
	}

	public static String getPathWithoutExt(String path) {
		int i = path.lastIndexOf('.');
		if (i == -1)
			return path;
		return path.substring(0, i);
	}

	public static String getExt(String path) {
		int i = path.lastIndexOf('.');
		if (i == -1)
			return "";
		return path.substring(i + 1);
	}

	public static String createUniquePath(String path) {
		String newPath = path;
		int i = 1;
		while (new File(newPath).exists()) {
			String pathWithoutExt = getPathWithoutExt(path);
			String ext = getExt(path);
			newPath = pathWithoutExt + "[" + i + "]." + ext;
			i++;
		}
		return newPath;
	}

	public static String append(String path, String s) {
		return getPathWithoutExt(path) + s + "." + getExt(path);
	}

	public static String getFileName(String path) {
		int i = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
		return path.substring(i + 1);
	}
}
