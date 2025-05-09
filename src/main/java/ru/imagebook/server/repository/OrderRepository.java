package ru.imagebook.server.repository;

import java.util.List;

import ru.imagebook.shared.model.*;

public interface OrderRepository {
    List<Order<?>> loadIncomingOrders(int userId);

    List<Order<?>> loadBasketOrders(int userId);

    Order<?> getOrder(int id);

    Order<?> getOrderByImportId(Integer importId);

    void save(Bill bill);

    void save(Order<?> order);

    List<Bill> loadBills(int userId);

    Bill getBill(int id);

    void deleteBill(Bill bill);

    List<Order<?>> loadOrders(int offset, int limit, OrderFilter filter, String query);

    long countOrders(OrderFilter filter, String query);

    List<Product> loadProducts();

    List<User> loadUsers(int offset, int limit, String query);

    long countUsers(String query);

    void saveOrder(Order<?> order);

    void deleteOrders(List<Integer> orderIds);

    void updateOrder(Order<?> order);

    Color getColor(int colorId);

    BonusCode findCode(String code, Vendor vendor);

    List<Order<?>> getCodeOrders(BonusCode bonusCode);

    List<Order<?>> loadOrders(OrderFilter filter);

    long getTrialOrdersCount(User user);

    String getLastOrderNumberByType(int orderType);

    List<Vendor> loadVendors();

    BonusCode getFirstCodeFromLastAction(Vendor vendor);

    void flush();

    /**
     * Метод получения списка счетов, у которых установлена дата самовывоза, но нотификаций по этим счетам еще не было
     * */
    List<Bill> loadBillsWithoutPickupDeliveryNotification();

    Order<?> getOrderByPublishCode(int code);

    List<Order<?>> findOrdersByState(Integer state);

    AlbumOrder findOrderbyAlbumId(String albumId);
}
