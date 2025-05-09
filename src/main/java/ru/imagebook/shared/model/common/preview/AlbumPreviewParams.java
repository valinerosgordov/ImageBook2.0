package ru.imagebook.shared.model.common.preview;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "album_preview")
public class AlbumPreviewParams implements Serializable {
    private String albumName;
    private int pageWidth;
    private int pageHeight;
    private int nPages;
    private boolean isHardOrEverflat;

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public int getnPages() {
        return nPages;
    }

    public void setnPages(int nPages) {
        this.nPages = nPages;
    }

    public boolean isHardOrEverflat() {
        return isHardOrEverflat;
    }

    public void setHardOrEverflat(boolean isHardOrEverflat) {
        this.isHardOrEverflat = isHardOrEverflat;
    }
}
