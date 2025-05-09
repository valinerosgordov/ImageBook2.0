package ru.imagebook.client.app.ctl.order;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.imagebook.shared.model.BonusAction;
import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Vellum;


public interface OrderPresenter {
    void loadIncomingOrders();

    void loadBasketOrders();

    void loadBasketOrders(List<Order<?>> basketOrders);

    void onOrderButtonClicked(Order<?> order);

    void moveOrdersToBasket(Set<Order<?>> orders);

    void removeOrderFromBasket(Order<?> order);

    void removeOrdersFromBasket(Set<Order<?>> orders);

    void onSubmitOrderButtonClicked();

    void submitOrder();

    void editOrder(Order<?> order);

    void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam);

    void enterBonusCode(Order<?> order);

    void prepareToApplyBonusCode(int orderId, String code, String deactivationCode);

    void applyBonusCode(int orderId, String code, String deactivationCode);

    void showActionCode(BonusAction action);

    void deleteOrders(Set<Order<?>> orders);

    void setFlyleaf(Order<?> order, Flyleaf flyleaf);

    Map<Flyleaf, Integer> computeFlyleafsPrices(Order<?> order, List<Flyleaf> flyleafs);

    void setVellum(Order<?> order, Vellum vellum);

    Map<Vellum, Integer> computeVellumsPrices(Order<?> order, List<Vellum> vellums);

    void createAlbumButtonClicked();

    void createAlbum(Integer productId, Integer pageCount);
}
