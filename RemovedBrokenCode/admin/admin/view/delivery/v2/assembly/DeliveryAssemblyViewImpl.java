package ru.imagebook.client.admin.view.delivery.v2.assembly;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FillLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

@Singleton
public class DeliveryAssemblyViewImpl implements DeliveryAssemblyView {
	private DeliveryAssemblyPresenter presenter;
	
	@Override
	public Widget asWidget() {
		LayoutContainer container1 = new LayoutContainer();
		container1.setLayout(new FillLayout(Orientation.HORIZONTAL));
		container1.add(presenter.getOrdersPresenter().getView().asWidget());
		container1.add(presenter.getShelfPresenter().getView().asWidget());
		
		LayoutContainer container2 = new LayoutContainer();
		container2.setLayout(new FillLayout(Orientation.HORIZONTAL));
		container2.add(presenter.getOrderInformationPresenter().getView().asWidget());
		container2.add(presenter.getWhatAreYouDoingPresenter().getView().asWidget());
		
		LayoutContainer container = new LayoutContainer();
		container.setLayout(new FillLayout(Orientation.VERTICAL));
		container.add(container1);
		container.add(container2);
		
		return container;
	}

	@Override
	public void setPresenter(DeliveryAssemblyPresenter deliveryAssemblyPresenter) {
		this.presenter = deliveryAssemblyPresenter;
	}

}
