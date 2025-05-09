package ru.imagebook.server.service2.app.delivery;

import ru.imagebook.shared.model.Bill;

/**
 * @author Sergey Boykov
 */
public class PickpointSendingIdGeneratorPrefixAndId implements PickpointSendingIdGenerator {

	private static final String ID_PREFIX = "imgb-";

	@Override
	public String generateImageBookSendingId(Bill bill) {
		return ID_PREFIX + bill.getId();
	}
}
