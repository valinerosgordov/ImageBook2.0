package ru.imagebook.server.service.backup;

public class BackupConfig {
	private String user;
	private String password;
	private String mysqldump;
	private String tar;
	private String path;
	private String ftpHost;
	private String ftpUser;
	private String ftpPass;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMysqldump() {
		return mysqldump;
	}

	public void setMysqldump(String mysqldump) {
		this.mysqldump = mysqldump;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getTar() {
		return tar;
	}

	public void setTar(String tar) {
		this.tar = tar;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public String getFtpUser() {
		return ftpUser;
	}

	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	public String getFtpPass() {
		return ftpPass;
	}

	public void setFtpPass(String ftpPass) {
		this.ftpPass = ftpPass;
	}
}
