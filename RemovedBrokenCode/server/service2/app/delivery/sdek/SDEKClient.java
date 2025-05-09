package ru.imagebook.server.service2.app.delivery.sdek;

import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.DeliveryResponse;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.request.CalculatePriceRequest;
import ru.imagebook.server.service2.app.delivery.sdek.model.calculation.response.CalculationPriceResponse;


public interface SDEKClient {
    DeliveryResponse sendRequestIM(DeliveryRequest deliveryRequest);

    CalculationPriceResponse sendCalculationRequest(CalculatePriceRequest calculatePriceRequest);
}