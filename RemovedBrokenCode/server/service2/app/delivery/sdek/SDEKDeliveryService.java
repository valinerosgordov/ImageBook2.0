package ru.imagebook.server.service2.app.delivery.sdek;

import java.util.List;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.SDEKPackage;


public interface SDEKDeliveryService {
    void start();

    List<SDEKPackage> computePackages(Bill bill);
}
