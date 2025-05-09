package ru.imagebook.client.common.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Vendor;


public interface VendorRemoteServiceAsync {
    void getVendor(AsyncCallback<Vendor> callback);
}