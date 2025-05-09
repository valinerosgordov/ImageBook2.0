package ru.imagebook.client.admin.view.delivery.v2.assembly;

import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;

public class WhatAreYouDoingViewImpl implements WhatAreYouDoingView {
	private DeliveryAssemblyConstants constants = GWT.create(DeliveryAssemblyConstants.class);
	
	private WhatAreYouDoingPresenter presenter;

	@Override
	public Widget asWidget() {
		return new Button(constants.whatAreYouDoing());
	}

	@Override
	public void setPresenter(WhatAreYouDoingPresenter presenter) {
		this.presenter = presenter;
	}

}
