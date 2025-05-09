package ru.imagebook.client.admin.view.delivery.v2.assembly;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class OrdersViewImpl implements OrdersView {
	private DeliveryAssemblyConstants constants = GWT.create(DeliveryAssemblyConstants.class);
	
	private OrdersPresenter presenter;

	@Override
	public Widget asWidget() {
		return new Button(constants.orders());
	}

	@Override
	public void setPresenter(OrdersPresenter presenter) {
		this.presenter = presenter;
	}

}
