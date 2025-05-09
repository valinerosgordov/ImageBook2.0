package ru.imagebook.server.service2.app.delivery;

import ru.imagebook.shared.model.Bill;

/**
 * @author Sergey Boykov
 */
public interface PickpointSendingIdGenerator {

	String generateImageBookSendingId(Bill bill);
}
