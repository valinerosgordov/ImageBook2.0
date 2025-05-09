package ru.imagebook.client.app.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKPackage;


public interface DeliveryRemoteServiceAsync {
    void loadMajorCities(String query, int limit, AsyncCallback<List<String>> callback);

    void getMajorCostAndTime(String majorCityName, int weightG, AsyncCallback<MajorData> callback);

    void getPickPointCostAndTime(PickPointData pickPointData, AsyncCallback<PickPointData> asyncCallback);

    void loadSDEKPackagesData(Bill bill, AsyncCallback<List<SDEKPackage>> asyncCallback);
}
