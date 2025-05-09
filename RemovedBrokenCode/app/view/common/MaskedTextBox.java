package ru.imagebook.client.app.view.common;

import com.google.gwt.dom.client.Element;


public class MaskedTextBox extends XTextBox {
    public MaskedTextBox() {
        super();
    }

    public MaskedTextBox(Element element) {
        super(element);
    }

    public void setDataInputMask(final String dataInputMask) {
        mask(getElement(), dataInputMask);
    }

    @Override
    public void setValue(String value) {
        val(getElement(), value);
    }

    @Override
    public String getValue() {
        return val(getElement());
    }

    private static native void mask(Element e, String maskString) /*-{
        $wnd.$(e).inputmask(maskString, {'autoUnmask' : true});
    }-*/;

    public static native String val(Element e) /*-{
        return $wnd.$(e).val();
    }-*/;

    public static native void val(Element e, String value) /*-{
        $wnd.$(e).val(value);
    }-*/;
}
