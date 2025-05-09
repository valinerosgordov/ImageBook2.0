package ru.imagebook.server.model.importing;

import java.io.Serializable;

public class XFtp implements Serializable {
	private String ftpHost;
    private String ftpUser;
    private String ftpPassword;

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

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }
}
