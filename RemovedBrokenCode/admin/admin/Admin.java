package ru.imagebook.client.admin;

import ru.imagebook.client.admin.ctl.AdminController;
import ru.imagebook.client.admin.ctl.AdminMessages;
import ru.imagebook.client.admin.ctl.DesktopController;
import ru.imagebook.client.admin.ctl.action.ActionController;
import ru.imagebook.client.admin.ctl.bill.BillController;
import ru.imagebook.client.admin.ctl.delivery.DeliveryController;
import ru.imagebook.client.admin.ctl.finishing.FinishingController;
import ru.imagebook.client.admin.ctl.mailing.MailingController;
import ru.imagebook.client.admin.ctl.order.OrderController;
import ru.imagebook.client.admin.ctl.pricing.PricingController;
import ru.imagebook.client.admin.ctl.product.AlbumController;
import ru.imagebook.client.admin.ctl.product.ColorController;
import ru.imagebook.client.admin.ctl.product.ProductImageController;
import ru.imagebook.client.admin.ctl.request.RequestController;
import ru.imagebook.client.admin.ctl.site.SiteController;
import ru.imagebook.client.admin.ctl.user.UserController;
import ru.imagebook.client.admin.ctl.vendor.VendorController;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.download.DownloadPostController;
import ru.minogin.core.client.flow.remoting.RemotingPostController;
import ru.saasengine.client.ctl.auth.AuthController;
import ru.saasengine.client.ctl.auth.AuthPreController;
import ru.saasengine.client.ctl.browser.BrowserController;
import ru.saasengine.client.ctl.failure.FailureController;

import com.google.inject.Inject;

public class Admin {
	private final Dispatcher dispatcher;
	private AdminController adminController;
	private DesktopController adminDesktopController;
	private VendorController agentController;
	private AuthController authController;
	private AuthPreController authPreController;
	private BrowserController browserController;
	private FailureController failureController;
	private RemotingPostController remotingPostController;
	private UserController userController;
	private PricingController pricingController;
	private AlbumController albumController;
	private ProductImageController productImageController;
	private ColorController colorController;
	private OrderController orderController;
	private RequestController actController;
	private DownloadPostController downloadPostController;
	private ActionController actionController;
	private BillController billController;
	private SiteController siteController;
	private DeliveryController deliveryController;
	private FinishingController finishingController;
	private MailingController mailingController;

	@Inject
	public Admin(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Inject
	public void setAdminController(AdminController adminController) {
		this.adminController = adminController;
	}

	@Inject
	public void setAgentController(VendorController agentController) {
		this.agentController = agentController;
	}

	@Inject
	public void setAdminDesktopController(DesktopController adminDesktopController) {
		this.adminDesktopController = adminDesktopController;
	}

	@Inject
	public void setFailureController(FailureController failureController) {
		this.failureController = failureController;
	}

	@Inject
	public void setAuthController(AuthController authController) {
		this.authController = authController;
	}

	@Inject
	public void setAuthPreController(AuthPreController authPreController) {
		this.authPreController = authPreController;
	}

	@Inject
	public void setBrowserController(BrowserController browserController) {
		this.browserController = browserController;
	}

	@Inject
	public void setRemotingPostController(RemotingPostController remotingPostController) {
		this.remotingPostController = remotingPostController;
	}

	@Inject
	public void setUserController(UserController userController) {
		this.userController = userController;
	}

	@Inject
	public void setPricingController(PricingController pricingController) {
		this.pricingController = pricingController;
	}

	@Inject
	public void setAlbumController(AlbumController albumController) {
		this.albumController = albumController;
	}

	@Inject
	public void setProductImageController(ProductImageController productImageController) {
		this.productImageController = productImageController;
	}

	@Inject
	public void setColorController(ColorController colorController) {
		this.colorController = colorController;
	}

	@Inject
	public void setOrderController(OrderController orderController) {
		this.orderController = orderController;
	}

	@Inject
	public void setActController(RequestController actController) {
		this.actController = actController;
	}

	@Inject
	public void setDownloadPostController(DownloadPostController downloadPostController) {
		this.downloadPostController = downloadPostController;
	}

	@Inject
	public void setActionController(ActionController actionController) {
		this.actionController = actionController;
	}

	@Inject
	public void setBillController(BillController billController) {
		this.billController = billController;
	}

	@Inject
	public void setSiteController(SiteController siteController) {
		this.siteController = siteController;
	}

	@Inject
	public void setDeliveryController(DeliveryController deliveryController) {
		this.deliveryController = deliveryController;
	}

	@Inject
	public void setFinishingController(FinishingController finishingController) {
		this.finishingController = finishingController;
	}

	@Inject
	public void setMailingController(MailingController mailingController) {
		this.mailingController = mailingController;
	}

	public void start() {
		authPreController.registerHandlers();

		adminController.registerHandlers();
		adminDesktopController.registerHandlers();
		agentController.registerHandlers();
		authController.registerHandlers();
		browserController.registerHandlers();
		failureController.registerHandlers();
		userController.registerHandlers();
		pricingController.registerHandlers();
		albumController.registerHandlers();
		colorController.registerHandlers();
		orderController.registerHandlers();
		actController.registerHandlers();
		actionController.registerHandlers();
		billController.registerHandlers();
		siteController.registerHandlers();
		deliveryController.registerHandlers();
		finishingController.registerHandlers();
		mailingController.registerHandlers();

		downloadPostController.registerHandlers();
		remotingPostController.registerHandlers();

		dispatcher.send(new BaseMessage(AdminMessages.START));

		// dispatcher.send(new LoginMessage(new Credentials("andrey", "passwd")));
		// dispatcher.send(new LoginMessage(new Credentials("admin", "passwd")));
		// dispatcher.send(new LoginMessage(new Credentials("mitya", "passwd")));
		// dispatcher.send(new LoginMessage(new Credentials("kolyan", "passwd")));
		// dispatcher.send(new LoginMessage(new Credentials("sveta", "passwd")));
	}
}
