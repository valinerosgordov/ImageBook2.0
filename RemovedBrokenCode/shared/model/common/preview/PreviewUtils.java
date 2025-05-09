package ru.imagebook.shared.model.common.preview;

import ru.imagebook.server.service.flash.PageType;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.Vendor;

public class PreviewUtils {
    public static String getImageUrl(int page, int pageSize, int nPages, boolean isSeparateCover, String sessionId,
                                     Vendor vendor) {
        String b = String.valueOf(PageType.NORMAL);
        String c = String.valueOf(pageSize);
        String d = String.valueOf(page);
        if (isSeparateCover) {
            if (page == 1 || page == 2) {
                b = String.valueOf(PageType.FRONT);
            } else if (page == (nPages - 1) || page == nPages) {
                b = String.valueOf(PageType.BACK);
                if (page == nPages) {
                    d = "2";
                } else {
                    d = "1";
                }
            } else {
                d = String.valueOf(page - 2);
            }
        }
        String url = "http://flash." + vendor.getEnglishDomain() + "/image?a=" + sessionId + "&b=" + b + "&c=" + c + "&d=" + d;
        //String url = "http://127.0.0.1:8888/flash/image?a=" + sessionId + "&b=" + b + "&c=" + c + "&d=" + d;
        return url;
    }
}
