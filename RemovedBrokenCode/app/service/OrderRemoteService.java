package ru.imagebook.client.app.service;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.shared.service.app.IncorrectPeriodCodeError;
import ru.minogin.core.client.bean.BaseBean;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**

 * @since 03.12.2014
 */
@RemoteServiceRelativePath("order.remoteService")
public interface OrderRemoteService extends RemoteService {
    List<Color> loadProductColors();

    List<Order<?>> loadIncomingOrders();

    List<Order<?>> loadBasketOrders(List<Order<?>> clientBasketOrders);

    void moveOrderToBasket(int orderId);

    void moveOrdersToBasket(Set<Order<?>> orders);

    void removeOrderFromBasket(int orderId);

    void removeOrdersFromBasket(Set<Order<?>> orders);

    void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam);

    BonusAction prepareToApplyBonusCode(int orderId, String code, String deactivationCode)
            throws IncorrectCodeError, IncorrectPeriodCodeError;

    void applyBonusCode(int orderId, String code, String deactivationCode);

    Bill submitOrder(List<Order<?>> basketOrders);

    /**
     * Temporarily solution for existing trial orders without weight.
     * Sets missing necessary parameters like itemWeight, totalWeight and so on.
     *
     * @param order
     */
    Order<?> fixTrialOrder(Order<?> order);

    Bill createBillForTrialOrder(int orderId, Bill deliveryParams, Integer userId);

    void editOrder(int orderId);

    void copyOrder(int orderId);

    void processOnlineOrder(int orderId);

    void deleteOrder(int orderId);

    void deleteOrders(Set<Order<?>> orders);

    Integer publishOrder(int orderId);

    void moveAnonymousOrders(String anonymousUserName);

    BaseBean attachAddress(Bill bill, Integer orderId, Address address);

    Order<?> attachAddressToTrial(Bill bill, Integer orderId, Address address);

    void orderTrial(Integer orderId);

    void markBillAsAnAdvertisingAndPaid(Bill bill);

    void notifyAddressCommentSpecified(Collection<Order<?>> orders, Address address);

    List<Flyleaf> loadFlyleafs();

    void setFlyleaf(Integer orderId, Integer flyleafId);

    List<Vellum> loadVellums();

    void setVellum(Integer orderId, Integer vellumId);

    Map<Integer, List<Product>> loadProducts();

    String createAlbum(Integer productId, Integer pageCount);

    String getAuthToken();
}
