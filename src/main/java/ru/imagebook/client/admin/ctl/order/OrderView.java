package ru.imagebook.client.admin.ctl.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.admin.view.order.OrderPresenter;
import ru.imagebook.shared.model.*;

public interface OrderView {
	void showProductSelectForm(Map<Integer, List<Product>> products, String locale);

	void showAddForm(Order<?> order, String locale, List<Color> colors, List<BonusAction> actions,
					 List<Flyleaf> flyleafs, List<Vellum> vellums);

	void showEditForm(Order<?> order, String locale, List<Color> colors, List<BonusAction> actions,
					  List<Flyleaf> flyleafs, List<Vellum> vellums);

	void showUsers(List<User> users, int offset, int total);

	void hideAddForm();

	void reload();

	void confirmDeleteOrders(List<Order<?>> orders);

	void alertNoOrdersToDelete();

	void hideEditForm();

	void showOrders(List<Order<?>> orders, int offset, int total, String locale, OrderFilter filter);

	void infoNotifyNewOrderResult();

	void alertOrderStateIsNotModeration();

	void infoNotifyOrdersAcceptedResult();

	void alertOrderStateIsNotAccepted();

	void alertTrialOrderExists();

	void alertOrderNumberExists();

	void infoGenerateFlashResult();

	void alertGenerateFlashFailed();

	void showOrdersSection(boolean colorize, boolean canDelete, String sessionId);

	void infoGeneratePdfResult();

	void infoPublishWebFlashResult();

	void informExportStarted();

	void setPresenter(OrderPresenter presenter);

	void showFilter(OrderFilter filter, String locale, Collection<Vendor> vendors);

	List<Order<?>> getSelectedOrders();
	
	void showBulkEditForm(String locale);
	
	void fetchBulk(List<Order<?>> orders);
	
	void hideBulkEditForm();

	void alertPublishOrderFailed();
}
