package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;

public interface ActionRepository {
	List<BonusAction> loadActions(String query);

	void saveAction(BonusAction action);

	void deleteActions(List<Integer> ids);

	BonusAction getAction(int actionId);

	long countCodesStartingWith(String code);

	List<StatusRequest> loadStatusRequests();

	void save(StatusRequest request);

	StatusRequest getLastRequest(User user);

	StatusRequest getRequest(int id);

	List<BonusAction> loadActions(Vendor vendor, String query);

	/**
	 * Метод удаления списка кодов по их id
	 * @param ids список id бонусных кодов
	 * */
	void deleteBonusCodes(List<Integer> ids);

	BonusAction getBonusActionWithAlbums(BonusAction bonusAction);
}
