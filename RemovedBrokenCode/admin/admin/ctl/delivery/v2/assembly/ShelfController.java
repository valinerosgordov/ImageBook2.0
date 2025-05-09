package ru.imagebook.client.admin.ctl.delivery.v2.assembly;

import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.ShelfPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.ShelfView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.ShelfViewImpl;

public class ShelfController implements ShelfPresenter {
	private ShelfView view;
	private DeliveryAssemblyPresenter presenter;
	
	public ShelfController(DeliveryAssemblyPresenter presenter) {
		view = new ShelfViewImpl();
		view.setPresenter(this);
		this.presenter = presenter;
	}

	@Override
	public ShelfView getView() {
		return view;
	}

}
