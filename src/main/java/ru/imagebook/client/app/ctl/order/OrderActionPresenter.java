package ru.imagebook.client.app.ctl.order;

import java.util.EnumSet;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

/**

 * @since 18.02.2015
 */
public interface OrderActionPresenter {
    interface SuccessCallback {
        void onSuccess();
    }

    Widget show();

    void addOrderAction(OrderAction orderAction);

    void addOrderActions(List<OrderAction> orderActions);

    void onProcessOrderButtonClicked();

    void onEditOrderButtonClicked();

    void onCopyOrderButtonClicked();

    void onDeleteOrderButtonClicked();

    void onDeleteOrderConfirmButtonClicked();

    void onPublishOrderButtonClicked();

    void addOrderActionListener(OrderAction orderAction, SuccessCallback callback);

    void addOrderActionListener(EnumSet<OrderAction> orderActions, SuccessCallback callback);

}
