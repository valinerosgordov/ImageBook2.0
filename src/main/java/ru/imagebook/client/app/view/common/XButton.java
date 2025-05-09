package ru.imagebook.client.app.view.common;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;


public class XButton extends Button {
    public XButton() {
        super();
    }

    public XButton(String html) {
        super(html);
    }

    public void setDataDismiss(String dataDismiss) {
        DOM.setElementAttribute(getElement(), "data-dismiss", dataDismiss);
    }

    public void setAreaHidden(String areaHidden) {
        DOM.setElementAttribute(getElement(), "aria-hidden", areaHidden);
    }
}
