package ru.imagebook.server.service2.app.delivery.sdek;

import java.util.List;

import ru.imagebook.client.common.service.delivery.SDEKDeliveryType;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryOrder;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryPackage;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.CalculatePriceRequest;
import ru.imagebook.shared.model.Bill;

public interface SDEKHelper {
    DeliveryRequest createDeliveryRequest(int billsCount);

    DeliveryOrder createDeliveryOrder(Bill bill, SDEKDeliveryType deliveryType);

    List<DeliveryPackage> computePackages(Bill bill);

    CalculatePriceRequest createCalculateTariffRequest(Bill bill, SDEKDeliveryType deliveryType, DeliveryOrder deliveryOrder);
}
