package ru.imagebook.server.service.pickbook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ru.imagebook.shared.model.Product.TRIAL_BB;
import static ru.imagebook.shared.model.Product.TRIAL_CC;
import ru.imagebook.server.model.importing.XOrder;
import ru.imagebook.server.model.importing.XUser;
import ru.imagebook.server.model.importing.XVendor;
import ru.imagebook.server.repository.OrderRepository;
import ru.imagebook.server.repository.ProductRepository;
import ru.imagebook.server.service.OrderService;
import ru.imagebook.server.service.VendorService;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.AlbumOrderImpl;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.OrderState;
import ru.imagebook.shared.model.OrderType;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;


@Service
public class PickbookOrderServiceImpl implements PickbookOrderService {

    @Autowired
    private PickbookUserService pickbookUserService;

    @Autowired
    private VendorService vendorService;

	@Autowired
    private OrderService orderService;

	@Autowired
	private PickbookHelper pickbookHelper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

	@Transactional
	@Override
	public void createOrder(XOrder xOrder) {
        XUser xUser = xOrder.getUser();
		XVendor xVendor = xUser.getVendor();
		Vendor vendor = vendorService.authenticateVendor(xVendor.getKey(), xVendor.getPassword());

        User user = pickbookUserService.getOrCreateUser(xUser, vendor);

        Album album = getAlbumByProductTypeAndNumber(xOrder.getBbInt(), xOrder.getCcInt());
        AlbumOrder order = new AlbumOrderImpl(album);

        order.setImportId(xOrder.getId());
        order.setNumber(xOrder.getNumber());
        order.setUser(user);
        order.setUrgent(user.isUrgentOrders());
        order.setType(OrderType.EXTERNAL);
        order.setPageCount(xOrder.getnPages());
        setOrderAlbumInfo(order, album);

        if (TRIAL_BB.equals(xOrder.getBb()) && TRIAL_CC.equals(xOrder.getCc())) {
            order.setTrial(true);
            order.setDeliveryType(DeliveryType.TRIAL);
        }

        orderService.setPricesAndCosts(order);
        orderService.addOrder(order);
	}

    @Transactional
    @Override
    public void updateOrder(XOrder xOrder) {
        XUser xUser = xOrder.getUser();
        XVendor xVendor = xUser.getVendor();
        vendorService.authenticateVendor(xVendor.getKey(), xVendor.getPassword());

        AlbumOrder order = (AlbumOrder) getOrderByImportId(xOrder.getId());
        Album album = getAlbumByProductTypeAndNumber(xOrder.getBbInt(), xOrder.getCcInt());

        order.setProduct(album);
        order.setPageCount(xOrder.getnPages());
        setOrderAlbumInfo(order, album);

        orderService.setPricesAndCosts(order);
        orderService.addOrder(order);
    }

    private Album getAlbumByProductTypeAndNumber(int type, int number) {
        Album album = productRepository.getAlbumByProductTypeAndNumber(type, number);
        Validate.notNull(album, "Album not found [type=%s, number=%s]", type, number);
        return album;
    }

    private void setOrderAlbumInfo(Order<?> order, Album album) {
        Map<Integer, Color> colors = getColorMap();
        order.setColor(colors.get(album.getColorRange().get(0)));
        order.setCoverLamination(album.getCoverLamRange().get(0));
        order.setPageLamination(album.getPageLamRange().get(0));
    }

    private Map<Integer, Color> getColorMap() {
        Map<Integer, Color> colors = new HashMap<Integer, Color>();
        List<Color> colorsList = productRepository.loadColors();
        for (Color color : colorsList) {
            colors.put(color.getNumber(), color);
        }
        return colors;
    }

    @Transactional
    @Override
    public void orderRenderStarted(XOrder xOrder) {
        XUser xUser = xOrder.getUser();
        XVendor xVendor = xUser.getVendor();
        vendorService.authenticateVendor(xVendor.getKey(), xVendor.getPassword());

        Validate.isTrue(xOrder.getId() > 0, "Album is not defined");

        Order<?> order = getOrderByImportId(xOrder.getId());
        order.setState(OrderState.JPEG_ONLINE_GENERATION);
    }

    @Transactional
    @Override
    public void orderRenderFinished(final XOrder xOrder) {
        XUser xUser = xOrder.getUser();
        XVendor xVendor = xUser.getVendor();
        vendorService.authenticateVendor(xVendor.getKey(), xVendor.getPassword());

        Validate.isTrue(xOrder.getId() > 0, "Album is not defined");

        final Order<?> order = getOrderByImportId(xOrder.getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                pickbookHelper.downloadJpegs(order, xOrder.getFtp(), xOrder.getJpegFolder());
            }
        }).start();
    }

    private Order<?> getOrderByImportId(int importId) {
        Order<?> order = orderRepository.getOrderByImportId(importId);
        Validate.notNull(order, "Order not found [importId=%s]", importId);
        return order;
    }
}