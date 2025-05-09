package ru.imagebook.client.admin.view.delivery.v2.assembly;

public interface DeliveryAssemblyPresenter {
	OrdersPresenter getOrdersPresenter();
	ShelfPresenter getShelfPresenter();
	OrderInformationPresenter getOrderInformationPresenter();
	WhatAreYouDoingPresenter getWhatAreYouDoingPresenter();
}
