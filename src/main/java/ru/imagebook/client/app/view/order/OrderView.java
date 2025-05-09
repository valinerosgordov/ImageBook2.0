package ru.imagebook.client.app.view.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.order.OrderPresenter;
import ru.imagebook.shared.model.*;


public interface OrderView extends IsWidget {
    void setPresenter(OrderPresenter presenter);

//    void showCreateAlbumButton(boolean show);

    void showIncomingOrders(List<Order<?>> incomingOrders, String locale);

    void showBasketOrders(List<Order<?>> basketOrders, String locale, List<Flyleaf> flyleafs, List<Vellum> vellums);

    Button getSubmitOrderButton();

    void showSubmitOrderConfirmation();

    void showOrderEditForm(Order<?> order, List<Color> colors, String locale);

    void hideOrderEditForm();

    void showOrderBonusCodeForm(Order<?> order);

    void hideOrderBonusCodeForm();

    void showConfirmActionCodeDialog(BonusAction action, Integer orderId, String code, String deactivationCode);

    void showConfirmActionCodeDialog(BonusAction action);

    void alertIncorrectCode();

    void alertIncorrectCodePeriod(Date dateStart, Date dateEnd);

    void alertProductNotAvailable();

    void alertCodeAlreadyUsed();

    void alertOrdersDeleted();

    void showCreateAlbumForm(Map<Integer, List<Product>> products);
}
