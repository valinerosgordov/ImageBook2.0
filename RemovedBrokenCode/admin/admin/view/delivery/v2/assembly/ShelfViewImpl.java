package ru.imagebook.client.admin.view.delivery.v2.assembly;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class ShelfViewImpl implements ShelfView {
	private DeliveryAssemblyConstants constants = GWT.create(DeliveryAssemblyConstants.class);
	
	private ShelfPresenter presenter;
	
	@Override
	public Widget asWidget() {
		return new Button(constants.shelf());
	}

	@Override
	public void setPresenter(ShelfPresenter presenter) {
		this.presenter = presenter;
	}

}
