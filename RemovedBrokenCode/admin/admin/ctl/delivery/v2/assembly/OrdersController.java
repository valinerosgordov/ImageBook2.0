package ru.imagebook.client.admin.ctl.delivery.v2.assembly;

import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrdersPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrdersView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrdersViewImpl;

public class OrdersController implements OrdersPresenter {
	private OrdersView view;
	private DeliveryAssemblyPresenter presenter;
	
	public OrdersController(DeliveryAssemblyPresenter presenter) {
		view = new OrdersViewImpl();
		view.setPresenter(this);
		this.presenter = presenter;
	}

	@Override
	public OrdersView getView() {
		return view;
	}

}
