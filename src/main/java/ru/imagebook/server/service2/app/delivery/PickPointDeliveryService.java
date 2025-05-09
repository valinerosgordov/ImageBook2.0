package ru.imagebook.server.service2.app.delivery;

import ru.imagebook.shared.model.app.PickPointData;

/**
 * @author Sergey Boykov
 */
public interface PickPointDeliveryService {

    PickPointData getZoneInfoByPostamateId(String postamateId);

    void start();
}
