package ru.imagebook.server.service.clean;

public interface CleanService {
	int SENT_ORDER_STORAGE_PERIOD_DAYS = 30;
	int NOT_PAID_ORDER_IN_7_DAYS = 7;
	int NOT_PAID_ORDER_IN_14_DAYS = 14;
	int NOT_PAID_ORDER_IN_20_DAYS = 20;
	int NOT_PAID_ORDER_IN_30_DAYS = 30;
	int NOT_PAID_ORDER_IN_60_DAYS = 60;
	int NOT_PAID_ORDER_IN_90_DAYS = 90;
	int NOT_PAID_ORDER_STORAGE_PERIOD_DAYS = 30;
	int NOT_PAID_BILLS_PERIOD_DAYS = 30;

	void clean();
}
