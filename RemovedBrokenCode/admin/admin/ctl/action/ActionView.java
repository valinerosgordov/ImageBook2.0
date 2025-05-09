package ru.imagebook.client.admin.ctl.action;

import java.util.List;
import java.util.Map;

import ru.imagebook.client.admin.view.action.ActionPresenter;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.BonusCode;
import ru.imagebook.shared.model.DeliveryDiscount;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.StatusRequest;
import ru.imagebook.shared.model.Vendor;

public interface ActionView {
	void setPresenter(ActionPresenter presenter);

	void showSection();

	void showActions(List<BonusAction> actions);

	void showCodes(BonusAction action, String sessionId);

	void showGenerateForm(BonusAction action);

	void informGenerateResult();

	void showStatusSection();

	void openRequest(StatusRequest request);

	void showStatusRequests(List<StatusRequest> requests, String locale);

	void showRejectForm(StatusRequest request);

	void showSendForm(StatusRequest request);

	void hideSendDialogs();

	void showActionSection(List<Vendor> vendors);

	void hideAddForm();

	void showActionForm(List<Album> albums, BonusAction bonusAction);

	void hideEditForm();

	void hideAddCodesForm();

	void informBadCode();

	void showBonusCodes(Map<BonusCode, List<Order<?>>> map);

	void loadBonusCodes();

	void alertBonusCodeOrderExists();

	void showDeliverySection(List<DeliveryDiscount> deliveryDiscounts);

	void addDeliveryDiscountToGrid(DeliveryDiscount deliveryDiscount);

	void alertDeliveryDiscountExists();

	void alertAddingDeliveryDiscountError();
}
