package ru.imagebook.client.app.view.common;

import com.google.gwt.dom.client.Element;


public final class Popover {
    private Popover() {
    }

    public static native void popover(Element e, String placement, String title, String content, String trigger) /*-{
        $wnd.jQuery(e).popover({
            placement: placement,
            title: title,
            content: content,
            trigger: trigger
        });
    }-*/;
}
