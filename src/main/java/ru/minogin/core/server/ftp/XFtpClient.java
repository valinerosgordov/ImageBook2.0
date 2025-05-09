package ru.minogin.core.server.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import ru.minogin.core.client.exception.Exceptions;

public class XFtpClient {
	public static final int TIMEOUT = 1000 * 30;

	private static Logger logger = Logger.getLogger(XFtpClient.class);

	private FTPClient client;

	public XFtpClient() {
		client = new FTPClient();
		client.setControlEncoding("UTF-8");
		client.setRemoteVerificationEnabled(false);
		logger.debug("Initialized");
	}

	public void connect(String host, String user, String pass) {
		try {
			client.setConnectTimeout(TIMEOUT);
			client.setDataTimeout(TIMEOUT);
			client.setDefaultTimeout(TIMEOUT);

			logger.debug("Connecting...");

			client.connect(host);

			logger.debug("Connected");
			int reply = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply))
				throw new RuntimeException("Can't connect. Reply code: " + reply);
			logger.debug("Reply code is: " + reply);

			client.setSoTimeout(TIMEOUT);

			client.login(user, pass);
			logger.debug("Logged in");
			client.setFileType(FTP.BINARY_FILE_TYPE);
			logger.debug("Set binary file type");
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public void disconnect() {
		if (client.isConnected()) {
			try {
				client.logout();
			}
			catch (Exception e) {
			}

			try {
				client.disconnect();
			}
			catch (Exception e) {
			}
			logger.debug("Disconnected");
		}
	}

	public FTPFile[] listFiles() {
		try {
			FTPFile[] files = client.listFiles();
			logger.debug("List files done");
			return files;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public void cd(String path) {
		try {
			boolean result = client.changeWorkingDirectory(path);
			if (result == false)
				throw new ChdirException(path);
			logger.debug("CD to " + path);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public String getFileContent(String path) {
		try {
			InputStream is = new BufferedInputStream(client.retrieveFileStream(path));
			logger.debug("Got stream");
			String content = IOUtils.toString(is, "UTF-8");
			logger.debug("Read");
			is.close();
			logger.debug("Closed stream");
			client.completePendingCommand();
			logger.debug("Completed");
			return content;
		}
		catch (Exception e) {
			return Exceptions.rethrow(e);
		}
	}

	public boolean fileExists(String path) {
		try {
			FTPFile[] files = client.listFiles(path);
			logger.debug("List files done");
			return files.length != 0;
		}
		catch (Exception e) {
			return (Boolean) Exceptions.rethrow(e);
		}
	}

	public boolean isFileReady(String path, int writeTimeSec) {
		try {
			FTPFile[] files = client.listFiles(path);
			logger.debug("List files done");
			if (files.length == 0)
				return false;
			FTPFile file = files[0];
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.SECOND, -writeTimeSec);
			return file.getTimestamp().before(calendar);
		}
		catch (Exception e) {
			return (Boolean) Exceptions.rethrow(e);
		}
	}

	public void loadFile(String fromPath, String toPath) {
		try {
			File toFile = new File(toPath);
			OutputStream toStream = new BufferedOutputStream(new FileOutputStream(
					toFile));
			client.retrieveFile(fromPath, toStream);
			logger.debug("File retrieved: " + fromPath + " > " + toPath);
			toStream.close();
			logger.debug("Stream closed");
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public void deleteFile(String path) {
		try {
			boolean result = client.deleteFile(path);
			if (result == false)
				throw new DeleteException();
			logger.debug("Deleted: " + path);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public void deleteFileIfExists(String path) {
		if (fileExists(path))
			deleteFile(path);
	}

	public void saveFile(String fromPath, String toPath) {
		try {
			File fromFile = new File(fromPath);
			InputStream fromStream = new BufferedInputStream(new FileInputStream(
					fromFile));
			boolean result = client.storeFile(toPath, fromStream);
			if (result == false)
				throw new SaveException();
			logger.debug("File stored: " + fromPath + " > " + toPath);
			fromStream.close();
			logger.debug("Stream closed");
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public void mkdir(String path) {
		try {
			client.mkd(path);
			logger.debug("Directory created: " + path);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}

	public void rename(String from, String to) {
		try {
			boolean result = client.rename(from, to);
			if (result == false)
				throw new RenameException();
			logger.debug("Renamed from " + from + " to " + to);
		}
		catch (Exception e) {
			Exceptions.rethrow(e);
		}
	}
}
