package ru.imagebook.client.app.view.order;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.order.OrderAction;
import ru.imagebook.client.app.ctl.order.OrderActionPresenter;

/**

 * @since 18.02.2015
 */
public interface OrderActionView extends IsWidget {
    void setPresenter(OrderActionPresenter presenter);

    void alertEditOrderFailed();

    void addOrderActionButton(OrderAction orderAction);

    void alertProcessOrderFailed();

    void alertCopyOrderFailed();

    void showDeleteOrderConfirmation();

    void alertOrderDeleted();

    void alertDeleteOrderFailed();

    void alertPublishOrderFailed();
}
