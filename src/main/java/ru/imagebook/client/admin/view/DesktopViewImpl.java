package ru.imagebook.client.admin.view;

import ru.imagebook.client.admin.ctl.BackupMessage;
import ru.imagebook.client.admin.ctl.CleanMessage;
import ru.imagebook.client.admin.ctl.DesktopView;
import ru.imagebook.client.admin.ctl.UpdateMessage;
import ru.imagebook.client.admin.ctl.action.ActionMessages;
import ru.imagebook.client.admin.ctl.bill.BillMessages;
import ru.imagebook.client.admin.ctl.country.CountryPlace;
import ru.imagebook.client.admin.ctl.delivery.DeliveryMessages;
import ru.imagebook.client.admin.ctl.delivery.v2.assembly.DeliveryAssemblyPlace;
import ru.imagebook.client.admin.ctl.finishing.FinishingMessages;
import ru.imagebook.client.admin.ctl.mailing.MailingMessages;
import ru.imagebook.client.admin.ctl.order.OrderMessages;
import ru.imagebook.client.admin.ctl.pricing.PricingMessages;
import ru.imagebook.client.admin.ctl.product.ProductMessages;
import ru.imagebook.client.admin.ctl.request.RequestMessages;
import ru.imagebook.client.admin.ctl.site.SiteMessages;
import ru.imagebook.client.admin.ctl.user.UserMessages;
import ru.imagebook.client.admin.ctl.vendor.VendorMessages;
import ru.imagebook.client.admin.ctl.zone.ZonePlace;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.browser.Browser;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.Desktop;
import ru.minogin.core.client.gxt.Workspace;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class DesktopViewImpl extends View implements DesktopView {
	private final Widgets widgets;
	private final MenuConstants menuConstants;
	private final CommonConstants appConstants;
	private final SecurityService securityService;
	@Inject
	private VendorService vendorService;
	private Workspace workspace;
	@Inject
	private PlaceController placeController;

	@Inject
	public DesktopViewImpl(Dispatcher dispatcher, Widgets widgets, MenuConstants menuConstants,
			CommonConstants appConstants, SecurityService securityService) {
		super(dispatcher);

		this.widgets = widgets;
		this.menuConstants = menuConstants;
		this.appConstants = appConstants;
		this.securityService = securityService;
	}

	@Override
	public void showDesktop() {
		Vendor vendor = vendorService.getVendor();
		Desktop desktop = new Desktop(vendor.getName() + " - система управления");
		createMenuItems(desktop.getToolBar());

		workspace = desktop.dispose();
		widgets.add(DesktopWidgets.DESKTOP, workspace);
	}

	private void createMenuItems(ToolBar toolBar) {
		if (securityService.isAllowed(Actions.ORDERS_VIEW)) {
			Button ordersButton = new Button(menuConstants.orders(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							placeController.goTo(Place.NOWHERE);
							send(OrderMessages.SHOW_ORDERS);
						}
					});
			toolBar.add(ordersButton);
		}

		if (securityService.isAllowed(Actions.BILLS)) {
			Button billsButton = new Button(menuConstants.billsButton(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							send(BillMessages.SHOW_SECTION);
						}
					});
			toolBar.add(billsButton);
		}

		if (securityService.isAllowed(Actions.USERS_VIEW)) {
			Button usersButton = new Button(menuConstants.users(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(UserMessages.SHOW_USERS);
				}
			});
			toolBar.add(usersButton);
		}

		if (securityService.isAllowed(Actions.PRODUCTS)) {
			Button productsButton = new Button(menuConstants.products());
			Menu menu = new Menu();
			MenuItem albumsItem = new MenuItem(menuConstants.albumsItem(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(ProductMessages.SHOW_ALBUMS);
						}
					});
			menu.add(albumsItem);
			MenuItem colorsItem = new MenuItem(menuConstants.colorsItem(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(ProductMessages.SHOW_COLORS);
						}
					});
			menu.add(colorsItem);

			MenuItem priceItem = new MenuItem(menuConstants.priceItem(),
					new SelectionListener<MenuEvent>() {
						@Override
						public void componentSelected(MenuEvent ce) {
							send(PricingMessages.EDIT_PRICING_DATA);
						}
					});
			menu.add(priceItem);

//			MenuItem weightReportItem = new MenuItem(menuConstants.weightReportItem(),
//					new SelectionListener<MenuEvent>() {
//						@Override
//						public void componentSelected(MenuEvent ce) {
//							Window.open(GWT.getHostPageBaseURL() + "weightReport", null, null);
//						}
//					});
//			menu.add(weightReportItem);

			productsButton.setMenu(menu);
			toolBar.add(productsButton);
		}

		if (securityService.isAllowed(Actions.REQUESTS)) {
			toolBar.add(new Button(menuConstants.acts(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(RequestMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.FINISHING)) {
			toolBar.add(new Button(menuConstants.finishing(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(FinishingMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.DELIVERY)) {
			toolBar.add(new Button(menuConstants.delivery(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(DeliveryMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.DISCOUNTS)) {
			toolBar.add(new Button(menuConstants.actions(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(ActionMessages.SHOW_ACTIONS);
				}
			}));
		}

		if (securityService.isAllowed(Actions.SITE)) {
			toolBar.add(new Button(menuConstants.siteButton(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(SiteMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.MAILING)) {
			toolBar.add(new Button(menuConstants.mailingButton(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(MailingMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.VENDORS)) {
			toolBar.add(new Button(menuConstants.agentsButton(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(VendorMessages.SHOW_SECTION);
				}
			}));
		}

		if (securityService.isAllowed(Actions.SERVER)) {
			Button button = new Button(menuConstants.serverButton());
			Menu menu = new Menu();
			menu.add(new MenuItem(menuConstants.backupButton(), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					new ConfirmMessageBox(appConstants.warning(), menuConstants.confirmBackup(),
							new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									send(new BackupMessage());
								}
							});
				}
			}));
			menu.add(new MenuItem(menuConstants.cleanButton(), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					new ConfirmMessageBox(appConstants.warning(), menuConstants.confirmClean(),
							new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									send(new CleanMessage());
								}
							});
				}
			}));
			menu.add(new MenuItem(menuConstants.updateButton(), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					new ConfirmMessageBox(appConstants.warning(), menuConstants.confirmUpdate(),
							new Listener<BaseEvent>() {
								@Override
								public void handleEvent(BaseEvent be) {
									send(new UpdateMessage());
								}
							});
				}
			}));
			button.setMenu(menu);
			toolBar.add(button);
		}

		if (securityService.isAllowed(Actions.DELIVERY_ASSEMBLY) || securityService.isAllowed(Actions.DELIVERY_ORDER)) {
			Button button = new Button(menuConstants.deliveryButton());
			Menu menu = new Menu();

			if (securityService.isAllowed(Actions.DELIVERY_ASSEMBLY)) {
				menu.add(new MenuItem(menuConstants.deliveryAssemblyButton(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						placeController.goTo(new DeliveryAssemblyPlace());
					}
				}));
			}

			if (securityService.isAllowed(Actions.DELIVERY_ORDER)) {
				menu.add(new MenuItem(menuConstants.deliveryOrderButton(), new SelectionListener<MenuEvent>() {
					@Override
					public void componentSelected(MenuEvent ce) {
						//placeController.goTo(new CountryPlace());
					}
				}));
			}

			button.setMenu(menu);
			toolBar.add(button);
		}

		if (securityService.isAllowed(Actions.MANAGEMENT)) {
			Button button = new Button(menuConstants.managementButton());
			Menu menu = new Menu();
			MenuItem item = new MenuItem(menuConstants.deliveryButton());
		    Menu sub = new Menu();
		    sub.add(new MenuItem(menuConstants.countriesButton(), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					placeController.goTo(new CountryPlace());
				}
			}));
		    sub.add(new MenuItem(menuConstants.zonesButton(), new SelectionListener<MenuEvent>() {
				@Override
				public void componentSelected(MenuEvent ce) {
					placeController.goTo(new ZonePlace());
				}
			}));
		    item.setSubMenu(sub);
		    menu.add(item);
			button.setMenu(menu);
			toolBar.add(button);
		}

        toolBar.add(new Button(menuConstants.logoutButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                logout();
            }
        }));
	}

	@Override
	public Workspace getWorkspace() {
		return workspace;
	}

    private void logout() {
        Browser.goTo(GWT.getHostPageBaseURL() + "logout");
    }
}
