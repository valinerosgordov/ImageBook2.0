package ru.minogin.util.server.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import ru.minogin.util.shared.exceptions.Exceptions;

/** File utilities.
 * 
 * @author Andrey Minogin */
public class FileUtil {
	/** Read a file into a string.
	 * @param file - file to read.
	 * @return file contents as string */
	public static String asString(File file) {
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

	/** Cut extension from path.
	 * @param path - path to a file, e.g. "d:/home/images/cat.jpg"
	 * @return path without extension (e.g. "d:/home/images/cat") */
	public static String getPathWithoutExtension(String path) {
		int i = path.lastIndexOf('.');
		if (i == -1)
			return path;
		return path.substring(0, i);
	}

	/** Get file extension.
	 * @param path - path to a file, e.g. "d:/home/images/cat.jpg"
	 * @return extension of a file, e.g. "jpg" */
	public static String getExtension(String path) {
		int i = path.lastIndexOf('.');
		if (i == -1)
			return "";
		return path.substring(i + 1);
	}

	/** <p>Given a path guaranteed to create a unique similar path with Windows
	 * style.</p>
	 * <p>Consider a path is "d:/home/images/cat.jpg" and no file exists at this
	 * path. Then this method just returns "d:/home/images/cat.jpg".<br/>If there is
	 * already file "cat.jpg" at this location then this method returns
	 * "d:/home/images/cat[1].jpg".<br/>
	 * If there is already files "cat.jpg" and "cat[1].jpg" at this location then
	 * this method returns "d:/home/images/cat[2].jpg" and so on.</p>
	 * @param path - a path to a file.
	 * @return a similar Windows-style unique path */
	public static String createUniquePath(String path) {
		String newPath = path;
		int i = 1;
		while (new File(newPath).exists()) {
			String pathWithoutExt = getPathWithoutExtension(path);
			String ext = getExtension(path);
			newPath = pathWithoutExt + "[" + i + "]." + ext;
			i++;
		}
		return newPath;
	}

	/**
	 * Appends a string to a file name before extension.
	 */
	public static String appendToFileName(String path, String s) {
		return getPathWithoutExtension(path) + s + "." + getExtension(path);
	}

	/**
	 * Returns file name.
	 */
	public static String getFileName(String path) {
		int i = Math.max(path.lastIndexOf("/"), path.lastIndexOf("\\"));
		return path.substring(i + 1);
	}
}
