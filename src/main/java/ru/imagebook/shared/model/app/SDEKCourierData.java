package ru.imagebook.shared.model.app;

import com.google.gwt.core.client.JavaScriptObject;

public class SDEKCourierData extends JavaScriptObject {
    protected SDEKCourierData() {
    }

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native int getCityId() /*-{
        return $wnd.parseInt(this.city);
    }-*/;

    public final native String getCityName() /*-{
        return this.cityName;
    }-*/;

    public final native int getPrice() /*-{
        return $wnd.parseInt(this.price);
    }-*/;

    public final native String getTerm() /*-{
        return this.term.toString();
    }-*/;

    public final native int getTarifCode() /*-{
        return this.tarif;
    }-*/;

    public final int getMinTerm() {
        return Integer.parseInt(getTerm().split("-")[0]);
    };
}
