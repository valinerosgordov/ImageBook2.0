package ru.imagebook.server.model.importing;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "order")
public class XOrder implements Serializable {
	private int id;
    private String number;
    private String bb;
    private String cc;
    private Integer nPages;
    private String jpegFolder;

    private XUser user;
    private XFtp ftp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBb() {
        return bb;
    }

    public int getBbInt() {
        return Integer.parseInt(bb);
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getCc() {
        return cc;
    }

    public int getCcInt() {
        return Integer.parseInt(cc);
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public Integer getnPages() {
        return nPages;
    }

    public void setnPages(Integer nPages) {
        this.nPages = nPages;
    }

    public String getJpegFolder() {
        return jpegFolder;
    }

    public void setJpegFolder(String jpegFolder) {
        this.jpegFolder = jpegFolder;
    }

    public XUser getUser() {
        return user;
    }

    public void setUser(XUser user) {
        this.user = user;
    }

    public XFtp getFtp() {
        return ftp;
    }

    public void setFtp(XFtp ftp) {
        this.ftp = ftp;
    }
}
