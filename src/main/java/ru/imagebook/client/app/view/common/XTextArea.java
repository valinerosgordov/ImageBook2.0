package ru.imagebook.client.app.view.common;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextArea;


public class XTextArea extends TextArea {
    public XTextArea() {
        super();
    }

    public XTextArea(Element element) {
        super(element);
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
