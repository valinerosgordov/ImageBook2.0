package ru.imagebook.client.app.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.imagebook.shared.model.*;
import ru.minogin.core.client.bean.BaseBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**

 * @since 03.12.2014
 */
public interface OrderRemoteServiceAsync {
    void loadProductColors(AsyncCallback<List<Color>> callback);

    void loadIncomingOrders(AsyncCallback<List<Order<?>>> callback);

    void loadBasketOrders(List<Order<?>> clientBasketOrders, AsyncCallback<List<Order<?>>> callback);

    void moveOrderToBasket(int orderId, AsyncCallback<Void> callback);

    void moveOrdersToBasket(Set<Order<?>> orders, AsyncCallback<Void> callback);

    void removeOrderFromBasket(int orderId, AsyncCallback<Void> callback);

    void removeOrdersFromBasket(Set<Order<?>> orders, AsyncCallback<Void> callback);

    void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam, AsyncCallback<Void> callback);

    void prepareToApplyBonusCode(int orderId, String code, String deactivationCode, AsyncCallback<BonusAction> callback);

    void applyBonusCode(int orderId, String code, String deactivationCode, AsyncCallback<Void> callback);

    void submitOrder(List<Order<?>> basketOrders, AsyncCallback<Bill> callback);

    void fixTrialOrder(Order<?> order, AsyncCallback<Order<?>> async);

    void createBillForTrialOrder(int orderId, Bill deliveryParams, Integer userId, AsyncCallback<Bill> async);

    void editOrder(int orderId, AsyncCallback<Void> callback);

    void copyOrder(int orderId, AsyncCallback<Void> callback);

    void processOnlineOrder(int orderId, AsyncCallback<Void> callback);

    void deleteOrder(int orderId, AsyncCallback<Void> callback);

    void deleteOrders(Set<Order<?>> orders, AsyncCallback<Void> callback);

    void publishOrder(int orderId, AsyncCallback<Integer> callback);

    void moveAnonymousOrders(String anonymousUserName, AsyncCallback<Void> async);

    void attachAddress(Bill bill, Integer orderId, Address address, AsyncCallback<BaseBean> callback);

    void attachAddressToTrial(Bill bill, Integer orderId, Address address, AsyncCallback<Order<?>> callback);

    void orderTrial(Integer orderId, AsyncCallback<Void> callback);

    void markBillAsAnAdvertisingAndPaid(Bill bill, AsyncCallback<Void> callback);

    void notifyAddressCommentSpecified(Collection<Order<?>> orders, Address address, AsyncCallback<Void> callback);

    void loadFlyleafs(AsyncCallback<List<Flyleaf>> asyncCallback);

    void setFlyleaf(Integer orderId, Integer flyleafId, AsyncCallback<Void> asyncCallback);

    void loadVellums(AsyncCallback<List<Vellum>> asyncCallback);

    void setVellum(Integer orderId, Integer vellumUd, AsyncCallback<Void> asyncCallback);

    void loadProducts(AsyncCallback<Map<Integer, List<Product>>> callback);

    void createAlbum(Integer productId, Integer pageCount, AsyncCallback<String> callback);

    void getAuthToken(AsyncCallback<String> callback);
}