package ru.imagebook.client.admin.ctl.delivery.v2.assembly;

import ru.imagebook.client.admin.service.DeliveryAssemblyServiceAsync;
import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.DeliveryAssemblyView;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrderInformationPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.OrdersPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.ShelfPresenter;
import ru.imagebook.client.admin.view.delivery.v2.assembly.WhatAreYouDoingPresenter;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DeliveryAssemblyActivity extends AbstractActivity implements DeliveryAssemblyPresenter {
	@Inject
	private DeliveryAssemblyView view;
	
	@Inject
	private DeliveryAssemblyServiceAsync service;
	
	private OrdersPresenter orders;
	private ShelfPresenter shelf;
	private OrderInformationPresenter orderInformation;
	private WhatAreYouDoingPresenter whatAreYouDoing;
	
	@Inject
	public DeliveryAssemblyActivity(DeliveryAssemblyView view) {
		view.setPresenter(this);
		this.view = view;
		orders = new OrdersController(this);
		shelf = new ShelfController(this);
		orderInformation = new OrderInformationController(this);
		whatAreYouDoing = new WhatAreYouDoingController(this);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		panel.setWidget(view);
	}

	@Override
	public OrdersPresenter getOrdersPresenter() {
		return orders;
	}

	@Override
	public ShelfPresenter getShelfPresenter() {
		return shelf;
	}

	@Override
	public OrderInformationPresenter getOrderInformationPresenter() {
		return orderInformation;
	}

	@Override
	public WhatAreYouDoingPresenter getWhatAreYouDoingPresenter() {
		return whatAreYouDoing;
	}

}
