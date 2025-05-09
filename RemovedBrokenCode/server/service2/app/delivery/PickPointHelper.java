package ru.imagebook.server.service2.app.delivery;

import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.PickPointData;

import java.util.List;
import java.util.Map;

/**
 * @author Sergey Boykov
 */
interface PickPointHelper {

	static final String IMAGEBOOK_WAREHOUSE_CITY = "Москва";
	static final String KEY_SESSION_ID = "SessionId";
	static final String KEY_ERROR_MESSAGE = "ErrorMessage";
	static final String KEY_ERROR = "Error";

	String login();

	boolean logout(String sessionId);

	Map<String, PickPointData> getZones();

	List<Bill> createSendings(List<Bill> bills);
}
