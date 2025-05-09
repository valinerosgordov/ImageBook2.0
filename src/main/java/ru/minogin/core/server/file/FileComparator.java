package ru.minogin.core.server.file;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {
	@Override
	public int compare(File f1, File f2) {
		String name1 = f1.getName();
		String name2 = f2.getName();
		try {
			Integer n1 = new Integer(FileUtil.getPathWithoutExt(name1));
			Integer n2 = new Integer(FileUtil.getPathWithoutExt(name2));
			return n1.compareTo(n2);
		}
		catch (NumberFormatException e) {
			return name1.compareToIgnoreCase(name2);
		}
	}
}
