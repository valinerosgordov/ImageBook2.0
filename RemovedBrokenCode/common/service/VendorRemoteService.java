package ru.imagebook.client.common.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Vendor;


@RemoteServiceRelativePath("vendor.remoteService")
public interface VendorRemoteService extends RemoteService {
    Vendor getVendor();
}