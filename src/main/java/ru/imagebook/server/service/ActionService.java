package ru.imagebook.server.service;

import java.io.Writer;
import java.util.List;
import java.util.Map;

import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.Vendor;

public interface ActionService {
	List<BonusAction> loadActions(String query);

	void updateAction(BonusAction action);

	void deleteActions(List<Integer> ids);

	void showCodes(int actionId, Writer writer);

	void generateCodes(int actionId, int quantity);

	List<StatusRequest> loadStatusRequests();

	void createStatusRequest(String request, int userId, StatusRequest.Source requestSource);

	int getRequestState(int userId);

	void rejectRequest(int requestId, String reason);

	void sendStatusCode(int requestId, String email);

	void activateRequest(int requestId, String code, Writer writer);

	BonusAction getBonusActionWithAlbums(BonusAction bonusAction);

	void addAction(BonusAction action);

	boolean addCodes(int actionId, String codes);

	List<BonusAction> loadActions(Vendor vendor, String query);

	/**
	 * Метод удаления списка кодов по их id из конкретной акции (bonusActionId)
	 * @param ids список id бонусных кодов
	 * @param  bonusActionId идентификатор акции, из которой будут удалены коды, заданные в ids
	 * */
	void deleteBonusCodesFromAction(List<Integer> ids, int bonusActionId);

	/**
	 * Метод получения словаря бонусных кодов со списком заказов, для которых задана конкретная акция (bonusActionId)
	 * @param  bonusActionId идентификатор акции, по которой сначала будут найдены все коды и из списка кодов будет сформирован список заказов
	 * */
	Map<BonusCode, List<Order<?>>> loadOrdersForBonusAction(int bonusActionId);

	List<Album> loadAlbums();
}
