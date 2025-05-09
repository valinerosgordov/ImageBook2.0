package ru.imagebook.client.common.util;


public class FlashUtils {
    private FlashUtils() {
    }

    public static native void showFlash(String url, String contextUrl, int width, int height, String sessionId) /*-{
        var f = new $wnd.com.deconcept.FlashObject(url + "/static/flash/flash.swf", "flashDiv", width, height, "7", "");
        f.addParam("FlashVars", "xmlFile=" + url + contextUrl + "/xml?a=" + sessionId);
        f.addParam("allowScriptAccess", "sameDomain");
        f.addParam("quality", "high");
        f.addParam("scale", "noscale");
        f.addParam("wmode", "transparent");
        f.write("flashDiv");
    }-*/;
}
