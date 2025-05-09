package ru.imagebook.client.app.view.process;

import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.shared.model.Order;

public interface ProcessView extends IsWidget{
    void showNoOrders();

    void showProcessingOrders(List<Order<?>> orders);
}
