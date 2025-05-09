package ru.imagebook.server.service.app;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.imagebook.client.app.service.OrderRemoteService;
import ru.imagebook.server.exchange.IExchangeService;
import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.server.model.importing.XVendor;
import ru.imagebook.server.repository.*;
import ru.imagebook.server.service.BillService;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.ProductService;
import ru.imagebook.server.service.UserService;
import ru.imagebook.server.service.auth.AuthService;
import ru.imagebook.server.service.editor.EditorService;
import ru.imagebook.server.service.pickbook.PickbookClient;
import ru.imagebook.server.service2.weight.WeightService;
import ru.imagebook.shared.model.*;
import ru.imagebook.shared.service.app.IncorrectCodeError;
import ru.imagebook.shared.service.app.IncorrectPeriodCodeError;
import ru.minogin.core.client.bean.BaseBean;
import ru.minogin.core.client.bean.EntityMap;
import ru.minogin.core.client.common.AccessDeniedError;
import ru.minogin.core.server.hibernate.Dehibernate;

import java.util.*;

import static ru.imagebook.shared.model.Product.TRIAL_BB;
import static ru.imagebook.shared.model.Product.TRIAL_CC;

/**

 * @since 03.12.2014
 */
@Service
public class OrderRemoteServiceImpl implements OrderRemoteService {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BillService billService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PickbookClient pickbookClient;
    @Autowired
    private WeightService weightService;
    @Autowired
    private ProductService productService;
    @Autowired
    private EditorService editorService;
    @Autowired
    private EditorRepository editorRepository;
    @Autowired
    private FlyleafRepository flyleafRepository;
    @Autowired
    private VellumRepository vellumRepository;
    @Autowired
    IExchangeService exchangeService;

    @Dehibernate
    @Transactional
    @Override
    public List<Color> loadProductColors() {
        return productService.loadColors();
    }

    @Dehibernate
    @Transactional
    @Override
    public List<Order<?>> loadIncomingOrders() {
        int userId = authService.getCurrentUserId();
        List<Order<?>> orders = orderRepository.loadIncomingOrders(userId);
        orderService.setPricesAndCosts(orders);
        return orders;
    }

    @Dehibernate
    @Transactional
    @Override
    public List<Order<?>> loadBasketOrders(List<Order<?>> clientBasketOrders) {
        List<Order<?>> basketOrders = loadBasketOrders();

        // UPDATE clientBasketOrdersQuantity
        if (clientBasketOrders != null) {
            EntityMap<Order<?>> basketOrdersMap = new EntityMap<Order<?>>(basketOrders);
            for (Order<?> clientOrder : clientBasketOrders) {
                Order<?> order = basketOrdersMap.get(clientOrder.getId());
                order.setQuantity(clientOrder.getQuantity());
            }

            // Reload data from DB // TODO needed of reload??
            return loadBasketOrders();
        } else {
            return basketOrders;
        }
    }

    private List<Order<?>> loadBasketOrders() {
        int userId = authService.getCurrentUserId();
        List<Order<?>> orders = orderRepository.loadBasketOrders(userId);
        orderService.setPricesAndCosts(orders);
        return orders;
    }

    @Transactional
    @Override
    public void moveOrderToBasket(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);
        User user = order.getUser();
        if (user.getId() != authService.getCurrentUserId()) {
            throw new AccessDeniedError();
        }
        if (order.getState() != OrderState.FLASH_GENERATED) {
            throw new AccessDeniedError();
        }

        order.setState(OrderState.BASKET);
        order.setQuantity(1);
    }

    @Transactional
    @Override
    public void moveOrdersToBasket(Set<Order<?>> orders) {
        for (Order<?> order : orders) {
            // Перемещаем в корзину не пробные заказы для которых сформированы макеты, остальные игнорируем
            if (order.getState() == OrderState.FLASH_GENERATED && !order.isTrial()) {
                moveOrderToBasket(order.getId());
            }
        }
    }

    @Transactional
    @Override
    public void removeOrderFromBasket(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);
        User user = order.getUser();
        if (user.getId() != authService.getCurrentUserId()) {
            throw new AccessDeniedError();
        }

        order.setState(OrderState.FLASH_GENERATED);
    }

    @Transactional
    @Override
    public void removeOrdersFromBasket(Set<Order<?>> orders) {
        for (Order<?> order : orders) {
            removeOrderFromBasket(order.getId());
        }
    }

    @Transactional
    @Override
    public void setOrderParams(int orderId, Integer colorId, Integer coverLam, Integer pageLam) {
        Order<?> order = orderRepository.getOrder(orderId);
        User user = order.getUser();
        if (user.getId() != authService.getCurrentUserId()) {
            throw new AccessDeniedError();
        }
        orderService.setOrderParams(orderId, colorId, coverLam, pageLam);
    }

    @Transactional
    @Override
    public void setFlyleaf(Integer orderId, Integer flyleafId) {
        Order<?> order = orderRepository.getOrder(orderId);
        Flyleaf flyleaf = flyleafRepository.findById(flyleafId).get();
        order.setFlyleaf(flyleaf);
    }

    @Transactional
    @Override
    public void setVellum(Integer orderId, Integer vellumId) {
        Order<?> order = orderRepository.getOrder(orderId);

        Vellum vellum = Optional.ofNullable(vellumId)
            .map(vellumRepository::findById)
            .map(Optional::get)
            .orElse(null);
        order.setVellum(vellum);
    }

    @Override
    public BonusAction prepareToApplyBonusCode(int orderId, String code, String deactivationCode)
            throws IncorrectCodeError, IncorrectPeriodCodeError {

        User user = authService.getCurrentUser();
        /*ищем нужный BonusAction и если нашли, то передаем в диалог подтверждения данной скидки*/
        BonusCode bonusCode = orderService.getBonusCode(code, user);
        return bonusCode.getAction();
    }

    @Transactional
    @Override
    public void applyBonusCode(int orderId, String code, String deactivationCode) {
        User user = authService.getCurrentUser();
        orderService.applyCode(orderId, code, deactivationCode, user);
    }

    @Dehibernate
    @Transactional
    @Override
    public Bill submitOrder(List<Order<?>> clientBasketOrders) {
        User user = authService.getCurrentUser();

        if (clientBasketOrders.isEmpty()) {
            throw new RuntimeException("Cannot order empty bill.");
        }

        EntityMap<Order<?>> clientBasketOrdersMap = new EntityMap<Order<?>>(clientBasketOrders);

        Bill bill = new Bill(user, new Date());
        int billWeight = 0;
        List<Order<?>> basketOrders = orderRepository.loadBasketOrders(user.getId());

        for (Order<?> order : basketOrders) {
            Order<?> clientOrder = clientBasketOrdersMap.get(order.getId());
            order.setQuantity(clientOrder.getQuantity());

            AlbumOrder albumOrder = (AlbumOrder) order;
            order.setItemWeight(weightService.getItemWeight(albumOrder));
            int totalWeight = weightService.getTotalWeight(albumOrder);
            order.setTotalWeight(totalWeight);

            billWeight += totalWeight;
        }

        orderService.setPricesAndCosts(basketOrders); // OK

        bill.setWeight(billWeight);
        for (Order<?> order : basketOrders) {
            order.setState(OrderState.BILL);
            bill.addOrder(order);
        }
        orderService.save(bill);

        orderService.notifyNewBill(bill);

        orderService.setComputedValues(bill);

        return bill;
    }

    @Dehibernate
    @Transactional
    @Override
    public Order<?> fixTrialOrder(Order<?> order) {
        Order orderToFix = orderRepository.getOrder(order.getId());
        Product product = orderToFix.getProduct();
        if (orderToFix instanceof AlbumOrder && product != null && product.isTrialAlbum()) {
            if (orderToFix.getItemWeight() == null) {
                orderToFix.setItemWeight(weightService.getItemWeight((AlbumOrder) orderToFix));
            }
            if (orderToFix.getTotalWeight() == null) {
                if (orderToFix.getQuantity() == 0) {
                    orderToFix.setQuantity(1);
                }
                orderToFix.setTotalWeight(weightService.getTotalWeight((AlbumOrder) orderToFix));
            }
        }
        return orderToFix;
    }

    @Dehibernate
    @Transactional
    @Override
    public Bill createBillForTrialOrder(int orderId, Bill deliveryParams, Integer userId) {
        User user = userService.getUser(userId);
        Order<?> order = orderRepository.getOrder(orderId);
        if (!order.getUser().equals(user)) {
            throw new AccessDeniedError();
        }

        Bill bill = new Bill(user, new Date());
        order.setQuantity(1);
        AlbumOrder albumOrder = (AlbumOrder) order;
        order.setItemWeight(weightService.getItemWeight(albumOrder));
        int totalWeight = weightService.getTotalWeight(albumOrder);
        order.setTotalWeight(totalWeight);
        order.setPhCost(order.getPhPrice() * order.getQuantity());
        order.setState(OrderState.BILL);

        bill.setWeight(totalWeight);
        bill.setDeliveryType(deliveryParams.getDeliveryType());
        bill.setDeliveryCost(deliveryParams.getDeliveryCost());
        bill.setDeliveryComment(deliveryParams.getDeliveryComment());
        bill.setOrientDeliveryDate(deliveryParams.getOrientDeliveryDate());
        bill.setDeliveryTime(deliveryParams.getDeliveryTime());
        bill.addOrder(order);
        orderService.save(bill);

        return bill;
    }

    @Transactional
    @Override
    public void moveAnonymousOrders(String anonymousUserName) {
        User unregisteredUser = null;
        if (anonymousUserName != null) {
            unregisteredUser = userService.getUser(anonymousUserName);
        }

        int currentUserId = authService.getCurrentUserId();
        User currentUser = userService.getUser(currentUserId);

        if (unregisteredUser != null && currentUser != null && !unregisteredUser.isRegistered()) {
            for (Order<?> item : unregisteredUser.getOrders()) {
                Order<?> order = orderRepository.getOrder(item.getId());
                order.setUser(currentUser);
            }
            orderService.flush();
            userService.detach(unregisteredUser);

            authRepository.deleteAccount(unregisteredUser.getId());
            userService.deleteUser(unregisteredUser.getId());
        }

        pickbookClient.moveAnonymousAlbums(currentUser, anonymousUserName);
    }

    @Dehibernate
    @Transactional
    @Override
    public BaseBean attachAddress(Bill inputBill, Integer orderId, Address address) {
        User user = authService.getCurrentUser();

        if (address.getId() != null) {
            userService.updateAddress(address);
        } else if (inputBill == null || !inputBill.isPickupDelivery()) {
            user.addAddress(address);
        }

        if (inputBill == null) {
            return null;
        } else {
            Bill bill = billService.getBill(inputBill.getId());

            Validate.isTrue(bill.getUser().equals(user), "Access denied");
            Validate.notNull(inputBill.getDeliveryType(), "Bill.deliveryType is not defined");

            bill.setDeliveryType(inputBill.getDeliveryType());
            bill.setDeliveryCost(inputBill.getDeliveryCost());
            bill.setDeliveryDiscountPc(inputBill.getDeliveryDiscountPc());
            bill.setOrientDeliveryDate(inputBill.getOrientDeliveryDate());
            bill.setDeliveryComment(inputBill.getDeliveryComment());
            bill.setMultishipOrderId(inputBill.getMultishipOrderId());
            bill.setMultishipType(inputBill.getMultishipType());
            bill.setMshDeliveryService(inputBill.getMshDeliveryService());
            bill.setDeliveryTime(inputBill.getDeliveryTime());
            bill.setPickpointPostamateID(inputBill.getPickpointPostamateID());
            bill.setPickpointRateZone(inputBill.getPickpointRateZone());
            bill.setPickpointTrunkCoeff(inputBill.getPickpointTrunkCoeff());
            bill.setPickpointAddress(inputBill.getPickpointAddress());
            // reset discount in case it was set early (e.g. previous bill's payment method was money mail.ru)
            bill.setDiscountPc(0);
            bill.setDdeliveryCityId(inputBill.getDdeliveryCityId());
            bill.setDdeliveryType(inputBill.getDdeliveryType());
            bill.setDdeliveryCompanyId(inputBill.getDdeliveryCompanyId());
            bill.setDdeliveryCompanyName(inputBill.getDdeliveryCompanyName());
            bill.setDdeliveryPickupPointId(inputBill.getDdeliveryPickupPointId());
            bill.setSdekDeliveryType(inputBill.getSdekDeliveryType());
            bill.setSdekPickupPointId(inputBill.getSdekPickupPointId());
            bill.setSdekCityId(inputBill.getSdekCityId());
            bill.setSdekPickupPointAddress(inputBill.getSdekPickupPointAddress());
            bill.setSdekTarifCode(inputBill.getSdekTarifCode());

            bill.attachAddressToOrders(address);

            orderService.setComputedValues(bill);

            BaseBean ret = new BaseBean();
            ret.set("bill", bill);
            return ret;
        }
    }

    @Dehibernate
    @Transactional
    @Override
    public Order<?> attachAddressToTrial(Bill bill, Integer orderId, Address address) {
        Order<?> order = orderService.getOrder(orderId);
        order.attachAddress(address);
        Integer deliveryType;
        if (bill != null) {
            deliveryType = bill.getDeliveryType() != null ? bill.getDeliveryType() : DeliveryType.TRIAL;
        } else {
            deliveryType = DeliveryType.TRIAL;
        }
        order.setDeliveryType(deliveryType);
        return order;
    }

    @Transactional
    @Override
    public void orderTrial(Integer orderId) {
        User user = authService.getCurrentUser();
        Order<?> order = orderService.orderTrial(orderId, user);
        Bill bill = order.getBill();
        billService.markPaid(bill.getId(), false);
    }

    @Override
    public void markBillAsAnAdvertisingAndPaid(Bill bill) {
        markBillAsAnAdvertising(bill);
        billService.markPaid(bill.getId());
    }

    private void markBillAsAnAdvertising(Bill bill) {
        bill.setAdv(true);
        billService.updateBill(bill);
    }

    @Override
    public void notifyAddressCommentSpecified(Collection<Order<?>> orders, Address address) {
        orderService.notifyAddressCommentSpecified(orders, address);
    }

    @Transactional
    @Override
    public void editOrder(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);
        order.setState(OrderState.NEW);
        order.setModifiedDate(new Date());

        if (order.getType() == OrderType.BOOK) {
            exchangeService.editAlbum(order.getAlbumId());
        }

        if (order.isExternalOrder()) {
            pickbookClient.editAlbum(order.getImportId());
        }
    }

    @Override
    public void copyOrder(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);
        if (order.isExternalOrder()) {
            pickbookClient.copyAlbum(order.getImportId());
        }

        if (order.isOnlineEditorOrder()) {
            String albumId = exchangeService.copyAlbum(order.getAlbumId());
            copyOrder(order, albumId);
        }
    }

    private void copyOrder(Order<?> source, String onlineEditorAlbumId) {
        User user = authService.getCurrentUser();

        AlbumOrder order = new AlbumOrderImpl((Album) source.getProduct());
        order.setAlbumId(onlineEditorAlbumId);
        order.setNumber(editorRepository.nextCounter() + "");
        order.setUser(user);
        order.setUrgent(user.isUrgentOrders());
        order.setType(source.getType());
        order.setPageCount(source.getPageCount());
        order.setColor(source.getColor());
        order.setCoverLamination(source.getCoverLamination());
        order.setPageLamination(source.getPageLamination());

        editorRepository.saveOrder(order);
    }

    @Transactional
    @Override
    public void processOnlineOrder(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);

        if (order.getType() == OrderType.BOOK) {
            order.setState(OrderState.JPEG_BOOK_GENERATION);
            exchangeService.renderAlbum(order.getAlbumId());
        } else {
            order.setState(OrderState.JPEG_ONLINE_GENERATION);
            pickbookClient.processAlbum(order.getImportId());
        }
    }

    @Transactional
    @Override
    public void deleteOrder(int orderId) {
        Order<?> order = orderRepository.getOrder(orderId);
        User user = order.getUser();
        if (user.getId() != authService.getCurrentUserId()) {
            throw new AccessDeniedError();
        }

        order.setState(OrderState.DELETED);

        if (order.getAlbumId() != null) {
            exchangeService.deleteAlbum(order.getAlbumId());
        }
    }

    @Transactional
    @Override
    public void deleteOrders(Set<Order<?>> orders) {
        for (Order<?> order : orders) {
            deleteOrder(order.getId());
        }
    }

    @Transactional
    @Override
    public Integer publishOrder(int orderId) {
        return orderService.publishOrder(orderId);
    }

    @Transactional
    @Override
    public List<Flyleaf> loadFlyleafs() {
        return flyleafRepository.findByActiveTrueOrderByNumber();
    }

    @Transactional
    @Override
    public List<Vellum> loadVellums() {
        return vellumRepository.findByActiveTrueOrderByNumber();
    }

    @Dehibernate
    @Transactional
    @Override
    public Map<Integer, List<Product>> loadProducts() {
        User user = authService.getCurrentUser();
        Map<Integer, List<Product>> map = editorService.loadProductsMap(user.getId());
        return map;
    }

    @Transactional
    @Override
    public String createAlbum(Integer productId, Integer pageCount) {
        User user = authService.getCurrentUser();
        String albumId = exchangeService.createAlbum(productId, pageCount, user);

        Album album = (Album) editorRepository.getProduct(productId);
        AlbumOrder order = new AlbumOrderImpl(album);
        order.setAlbumId(albumId);
        order.setType(OrderType.BOOK);
        order.setNumber(editorRepository.nextCounter() + "");
        order.setUser(user);
        order.setUrgent(user.isUrgentOrders());
        order.setPageCount(pageCount);

        int colorNumber = album.getColorRange().get(0);
        Color color = editorRepository.getColor(colorNumber);
        order.setColor(color);

        order.setCoverLamination(album.getCoverLamRange().get(0));
        order.setPageLamination(album.getPageLamRange().get(0));

        editorRepository.saveOrder(order);

        return albumId;
    }

    @Transactional
    @Override
    public String getAuthToken() {
        User user = authService.getCurrentUser();
        return exchangeService.getAuthToken(user);
    }
}
