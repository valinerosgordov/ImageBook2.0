package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.minogin.core.client.bean.BaseBean;


public interface BillRemoteServiceAsync {
    void loadBills(AsyncCallback<BaseBean> callback);

    void deleteBill(int billId, AsyncCallback<Void> callback);
}