package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.minogin.core.client.bean.BaseBean;


@RemoteServiceRelativePath("bill.remoteService")
public interface BillRemoteService extends RemoteService {
    BaseBean loadBills();

    void deleteBill(int billId);
}