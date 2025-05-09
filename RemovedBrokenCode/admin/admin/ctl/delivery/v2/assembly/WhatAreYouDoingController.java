package ru.imagebook.client.admin.ctl.delivery.v2.assembly;

import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.WhatAreYouDoingPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.WhatAreYouDoingView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.WhatAreYouDoingViewImpl;

public class WhatAreYouDoingController implements WhatAreYouDoingPresenter {
	private WhatAreYouDoingView view;
	private DeliveryAssemblyPresenter presenter;
	
	public WhatAreYouDoingController(DeliveryAssemblyPresenter presenter) {
		view = new WhatAreYouDoingViewImpl();
		view.setPresenter(this);
		this.presenter = presenter;
	}

	@Override
	public WhatAreYouDoingView getView() {
		return view;
	}

}
