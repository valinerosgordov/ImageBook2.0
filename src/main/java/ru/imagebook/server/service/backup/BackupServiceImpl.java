package ru.imagebook.server.service.backup;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.minogin.core.server.ftp.XFtpClient;
import ru.minogin.core.server.system.MySQLDump;
import ru.minogin.core.server.system.Tar;

public class BackupServiceImpl implements BackupService {
	private final BackupConfig config;

	public BackupServiceImpl(BackupConfig config) {
		this.config = config;
	}

	@Override
	public void backup() {
		Date date = new Date();
		String datePattern = "yyyy-MM-dd--HH-mm";
		String dateText = new SimpleDateFormat(datePattern).format(date);

		String folder = config.getPath() + "/" + dateText;
		File folderFile = new File(folder);
		folderFile.mkdirs();

		String names[] = new String[] { "office", "wiki" };
		List<String> fileNames = new ArrayList<String>();

		MySQLDump dump = new MySQLDump(config.getMysqldump());
		for (String name : names) {
			String fileName = folder + "/" + name + ".sql";
			dump.dump(config.getUser(), config.getPassword(), name, fileName);
			fileNames.add(fileName);
		}

		Tar tar = new Tar(config.getTar());
		String tarPath = folder + ".tar.gz";
		tar.tarFolder(folder, tarPath);

		for (String fileName : fileNames) {
			new File(fileName).delete();
		}
		folderFile.delete();

		XFtpClient client = new XFtpClient();
		client.connect(config.getFtpHost(), config.getFtpUser(), config.getFtpPass());
		try {
			client.cd("backup/Imagebook");
			client.mkdir(dateText);
			client.cd(dateText);
			String tarFileName = dateText + ".tar.gz";
			client.saveFile(tarPath, tarFileName);
		}
		finally {
			client.disconnect();
		}
	}
}
