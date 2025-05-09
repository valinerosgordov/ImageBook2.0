package ru.minogin.bill.server.util;

import java.math.BigDecimal;

public class RoboUtil {
	public static int getIntSum(String sum) {
		return new BigDecimal(sum).intValue();
	}
}
