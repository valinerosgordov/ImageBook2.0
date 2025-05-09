package ru.imagebook.client.app.view.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;


public class Anchor extends com.google.gwt.user.client.ui.Anchor {
    private static final String DEFAULT_HREF = "javascript:;";

    public Anchor() {
        setHref(DEFAULT_HREF);
    }

    public Anchor(String text) {
        super(text);
    }

    public void setDataToggle(String toggle) {
        DOM.setElementAttribute(getElement(), "data-toggle", toggle);
    }

    public void setDataTarget(String target) {
        DOM.setElementAttribute(getElement(), "data-target", target);
    }

    /**
     * This disables the passing of the browser event to GWT's Anchor class (summarily disabling all related handlers)
     * when the link is double clicked, focused or clicked and is in a disabled state.
     * @link {http://code.google.com/p/google-web-toolkit/issues/detail?id=2889}
     */
    @Override
    public void onBrowserEvent(Event event) {
        switch (DOM.eventGetType(event)) {
            case Event.ONDBLCLICK:
                System.out.println("dbclick");
            case Event.ONFOCUS:
            case Event.ONCLICK:
                if (!isEnabled()) {
                    return;
                }
                break;
        }
        super.onBrowserEvent(event);
    }
}
