package ru.imagebook.client.admin.view.delivery.v2.assembly;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class OrderInformationViewImpl implements OrderInformationView {
	private DeliveryAssemblyConstants constants = GWT.create(DeliveryAssemblyConstants.class);
	
	private OrderInformationPresenter presenter;

	@Override
	public Widget asWidget() {
		return new Button(constants.orderInformation());
	}

	@Override
	public void setPresenter(OrderInformationPresenter presenter) {
		this.presenter = presenter;
	}

}
