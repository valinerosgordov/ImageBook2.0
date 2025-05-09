package ru.imagebook.server.service2.app.delivery;

import java.util.Set;

import ru.imagebook.shared.model.app.MajorData;


public interface MajorExpressService {

    Set<String> getCities();

    String getCityCode(String cityMajorName);

    MajorData getCostAndTime(String cityMajorName, int weightG);
}
