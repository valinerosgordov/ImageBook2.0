package ru.imagebook.client.app.view.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;


public class XTextBox extends TextBox {
    public XTextBox() {
        super();
    }

    public XTextBox(Element element) {
        super(element);
    }

    public void setPlaceholder(String placeholder) {
        DOM.setElementProperty(getElement(), "placeholder", placeholder);
    }

    @Override
    public String getValue() {
        String value = super.getValue();

        if (value != null) {
            value = value.trim();
        }

        if (value != null && value.isEmpty()) {
            value = null;
        }

        return value;
    }

    public boolean isEmpty() {
        String value = getValue();
        return value == null || value.length() == 0;
    }
}
