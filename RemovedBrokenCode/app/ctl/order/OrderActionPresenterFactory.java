package ru.imagebook.client.app.ctl.order;

import java.util.List;

import ru.imagebook.shared.model.Order;


public interface OrderActionPresenterFactory {
    OrderActionPresenter create(Order<?> order);

    OrderActionPresenter create(Order<?> order, List<OrderAction> orderActions);
}
