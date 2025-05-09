package ru.imagebook.client.app.view.common;

import com.google.gwt.dom.client.Element;


public class Affix {
    public static native void affix(final Element e, final int offset) /*-{
        $wnd.jQuery(e).affix({
            offset: offset
        });
    }-*/;
}
