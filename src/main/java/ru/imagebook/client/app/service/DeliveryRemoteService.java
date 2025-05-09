package ru.imagebook.client.app.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.MajorData;
import ru.imagebook.shared.model.app.PickPointData;
import ru.imagebook.shared.model.app.SDEKPackage;


@RemoteServiceRelativePath("delivery.remoteService")
public interface DeliveryRemoteService extends RemoteService {
    List<String> loadMajorCities(String query, int limit);

    MajorData getMajorCostAndTime(String majorCityName, int weightG);

    PickPointData getPickPointCostAndTime(PickPointData pickPointData) throws PostamateUnavailableException;

    List<SDEKPackage> loadSDEKPackagesData(Bill bill);
}
