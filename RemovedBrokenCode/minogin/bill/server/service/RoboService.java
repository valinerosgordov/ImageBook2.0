package ru.minogin.bill.server.service;

import ru.minogin.bill.shared.RoboConfig;
import ru.minogin.bill.shared.RoboInfo;

public interface RoboService {
	RoboInfo createInfo(RoboConfig config, int orderId, int sum, String desc);

	String createHTML(RoboConfig config, int orderId, int sum, String desc);

	void checkResult(RoboConfig config, int orderId, String sum, String crc);

	void checkSuccess(RoboConfig config, int orderId, String sum, String crc);
}
