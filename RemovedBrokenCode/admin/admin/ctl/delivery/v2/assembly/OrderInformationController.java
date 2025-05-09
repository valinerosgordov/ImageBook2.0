package ru.imagebook.client.admin.ctl.delivery.v2.assembly;

import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrderInformationPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrderInformationView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrderInformationViewImpl;

public class OrderInformationController implements OrderInformationPresenter {
	private OrderInformationView view;
	private DeliveryAssemblyPresenter presenter;
	
	public OrderInformationController(DeliveryAssemblyPresenter presenter) {
		view = new OrderInformationViewImpl();
		view.setPresenter(this);
		this.presenter = presenter;
	}

	@Override
	public OrderInformationView getView() {
		return view;
	}

}
