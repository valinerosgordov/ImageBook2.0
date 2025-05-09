package ru.imagebook.client.admin.ctl;

import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.admin.ctl.country.CountryActivity;
import ru.imagebook.client.admin.ctl.country.CountryPlace;
import ru.imagebook.client.admin.ctl.delivery.DeliveryMessages;
import ru.imagebook.client.admin.ctl.delivery.v2.assembly.DeliveryAssemblyActivity;
import ru.imagebook.client.admin.ctl.delivery.v2.assembly.DeliveryAssemblyPlace;
import ru.imagebook.client.admin.ctl.finishing.FinishingMessages;
import ru.imagebook.client.admin.ctl.order.OrderMessages;
import ru.imagebook.client.admin.ctl.product.ProductMessages;
import ru.imagebook.client.admin.ctl.site.SiteMessages;
import ru.imagebook.client.admin.ctl.site.tag.TagActivity;
import ru.imagebook.client.admin.ctl.site.tag.TagPlace;
import ru.imagebook.client.admin.ctl.zone.ZoneActivity;
import ru.imagebook.client.admin.ctl.zone.ZonePlace;
import ru.imagebook.client.common.service.AuthData;
import ru.imagebook.client.common.service.AuthServiceAsync;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.admin.Actions;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.minogin.core.client.flow.BaseMessage;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.mvp.SimpleActivityMapper;
import ru.minogin.core.client.push.PushService;
import ru.minogin.core.client.push.mvp.MvpPushListener;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.browser.PrepareBrowserMessage;
import ru.saasengine.client.ctl.desktop.DesktopMessages;
import ru.saasengine.client.service.app.AppService;
import ru.saasengine.client.service.auth.AuthService;

@Singleton
public class AdminController extends Controller {
	@Inject
	private AppService appService;
	@Inject
	private AdminView view;
	@Inject
	private SecurityService securityService;
	@Inject
	private AuthServiceAsync authService;
	@Inject
	private UserService userService;
	@Inject
	private AuthService authService2;
	@Inject
	private PushService pushService;
	@Inject
	private EventBus eventBus;
	@Inject
	private DesktopView desktopView;
	@Inject
	private TagActivity tagActivity;
	@Inject
	private CountryActivity countryActivity;
	@Inject
	private ZoneActivity zoneActivity;
	@Inject
	private DeliveryAssemblyActivity deliveryAssemblyActivity;
/*	@Inject
	private QuestionCategoryActivity questionCategoryActivity;*/
	@Inject
	private VendorService vendorService;

	@Inject
	public AdminController(Dispatcher dispatcher) {
		super(dispatcher);
	}

	private final SimpleActivityMapper activityMapper = new SimpleActivityMapper();

	@Override
	public void registerHandlers() {
		activityMapper.map(new TagPlace(), tagActivity);
		activityMapper.map(new CountryPlace(), countryActivity);
		activityMapper.map(new ZonePlace(), zoneActivity);
		activityMapper.map(new DeliveryAssemblyPlace(), deliveryAssemblyActivity);
		//activityMapper.map(new QuestionCategoryPlace(), questionCategoryActivity);

		addHandler(AdminMessages.START, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				String appName = appService.getAppName();
				send(new PrepareBrowserMessage(appName));

				authService.auth(new AsyncCallback<AuthData>() {
					@Override
					public void onSuccess(AuthData data) {
						userService.setUser(data.getUser());
						authService2.setSessionId(data.getSessionId());

						pushService.addListener(new MvpPushListener(eventBus));
						pushService.start();

						send(AuthMessages.SESSION_STARTED);
					}

					@Override
					public void onFailure(Throwable caught) {}
				});

				// send(new AuthMessage(new Credentials()));
			}
		});

		addHandler(AuthMessages.SESSION_STARTED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (securityService.isAllowed(Actions.ADMIN))
					send(new BaseMessage(DesktopMessages.SHOW_DESKTOP));
				else
					send(new BaseMessage(AdminMessages.PERMISSION_DENIED));
			}
		});

		addHandler(AdminMessages.PERMISSION_DENIED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.permissionDenied();
			}
		});

		addHandler(DesktopMessages.DESKTOP_SHOWN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (securityService.isAllowed(Actions.ORDERS_VIEW)) {
					send(OrderMessages.SHOW_ORDERS);

					// send(UserMessages.SHOW_USERS);
					// send(SiteMessages.SHOW_SECTION);
					// send(ActionMessages.SHOW_ACTIONS);
//					 send(FinishingMessages.SHOW_SECTION);
					// send(DeliveryMessages.SHOW_SECTION);
					// send(BillMessages.SHOW_SECTION);
					// send(RequestMessages.SHOW_SECTION);
//					 send(ProductMessages.SHOW_ALBUMS);
					// send(MailingMessages.SHOW_SECTION);
				}
				else if (securityService.isAllowed(Actions.SITE)) {
					send(SiteMessages.SHOW_SECTION);
					// send(UserMessages.SHOW_USERS);
					// send(ActionMessages.SHOW_ACTIONS);
				}
				else if (securityService.isAllowed(Actions.DELIVERY)) {
					send(DeliveryMessages.SHOW_SECTION);
				}
				else if (securityService.isAllowed(Actions.FINISHING)) {
					send(FinishingMessages.SHOW_SECTION);
				}

				ActivityManager activityManager = new ActivityManager(activityMapper,
						eventBus);
				activityManager.setDisplay(desktopView.getWorkspace());
			}
		});

		addHandler(AdminMessages.BACKUP_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.infoBackupComplete();
			}
		});

		addHandler(AdminMessages.CLEAN_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.infoCleanComplete();
			}
		});

		addHandler(AdminMessages.UPDATE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.infoUpdateComplete();
			}
		});
	}
}
