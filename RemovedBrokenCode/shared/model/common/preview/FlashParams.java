package ru.imagebook.shared.model.common.preview;

import java.io.Serializable;


public class FlashParams implements Serializable {
    private String sessionId;
    private int flashWidth;
    private int flashHeight;
    private String flashContextUrl;
    private int nPages;

    public FlashParams() {
    }

    public FlashParams(String sessionId, int flashWidth, int flashHeight, String flashContextUrl,int nPages){
        this.sessionId = sessionId;
        this.flashWidth = flashWidth;
        this.flashHeight = flashHeight;
        this.flashContextUrl = flashContextUrl;
        this.nPages=nPages;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getFlashWidth() {
        return flashWidth;
    }

    public int getFlashHeight() {
        return flashHeight;
    }

    public String getFlashContextUrl() {
        return flashContextUrl;
    }

    public int getnPages() {
        return nPages;
    }
}
