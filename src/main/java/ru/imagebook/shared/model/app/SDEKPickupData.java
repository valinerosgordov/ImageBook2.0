package ru.imagebook.shared.model.app;

import com.google.gwt.core.client.JavaScriptObject;

public class SDEKPickupData extends JavaScriptObject {
    protected SDEKPickupData() {
    }

    public final native String getId() /*-{
        return this.id;
    }-*/;

    public final native String getName() /*-{
        return this.PVZ.Name;
    }-*/;

    public final native String getAddress() /*-{
        return this.PVZ.Address;
    }-*/;

    public final native String getAddressComment() /*-{
        return this.PVZ.AddressComment;
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
