package ru.imagebook.client.editor.ctl;

import ru.imagebook.client.common.ctl.register.RegisterView;
import ru.imagebook.client.common.service.AuthData;
import ru.imagebook.client.common.service.AuthServiceAsync;
import ru.imagebook.client.common.service.BannerRemoteService;
import ru.imagebook.client.common.service.BannerRemoteServiceAsync;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.editor.ctl.file.FileMessages;
import ru.imagebook.client.editor.ctl.order.OpenOrderMessage;
import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;
import ru.minogin.core.client.gwt.ClientParametersReader;
import ru.saasengine.client.ctl.auth.AuthMessages;
import ru.saasengine.client.ctl.auth.ConnectMessage;
import ru.saasengine.client.ctl.auth.ReconnectMessage;
import ru.saasengine.client.ctl.browser.PrepareBrowserMessage;
import ru.saasengine.client.ctl.desktop.DesktopMessages;
import ru.saasengine.client.service.app.AppService;
import ru.saasengine.client.service.auth.AuthService;

import com.extjs.gxt.ui.client.GXT;
import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EditorController extends Controller {
	private final AppService appService;
	private final EditorView view;
	
	public static final int CONNECT_DELAY_MS = 1000;
	private boolean connected;
	
	@Inject
	private AuthService authService2;
	@Inject
	private AuthServiceAsync authService;
	@Inject
	private UserService userService;
	@Inject
	private RegisterView registerView;

	private final BannerRemoteServiceAsync bannerService = GWT.create(BannerRemoteService.class);
	
	@Inject
	public EditorController(Dispatcher dispatcher, AppService appService, EditorView view) {
		super(dispatcher);

		this.appService = appService;
		this.view = view;
	}

	@Override
	public void registerHandlers() {
		addHandler(EditorMessages.START, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				ClientParametersReader reader = new ClientParametersReader();
				if (reader.getParam("login") != null) {
					registerView.showLinks(Module.Editor);
				} else {
					send(EditorMessages.START_APP);
				}
			}	
		});
		
		addHandler(EditorMessages.START_APP, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				if (GXT.getUserAgent().contains("msie")) {
					view.alertIE();
					return;
				}
	
				String appName = appService.getAppName();
				send(new PrepareBrowserMessage(appName));
	
				authService.auth(new AsyncCallback<AuthData>() {
					@Override
					public void onSuccess(AuthData data) {
						userService.setUser(data.getUser());
						authService2.setSessionId(data.getSessionId());
						
						connected = true;
						
						new Timer() {
							@Override
							public void run() {
								send(new ConnectMessage());
							}
						}.schedule(CONNECT_DELAY_MS);
						
						send(AuthMessages.SESSION_STARTED);
					}
	
					@Override
					public void onFailure(Throwable caught) {}
				});
			}
		});		
		
		addHandler(AuthMessages.RECONNECT, new MessageHandler<ReconnectMessage>() {
			@Override
			public void handle(ReconnectMessage message) {
				if (connected)
					send(new ConnectMessage());
			}
		});
		
		addHandler(AuthMessages.CONCURRENT_LOGIN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				connected = false;
			}
		});

		addHandler(AuthMessages.SESSION_STARTED, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				send(DesktopMessages.SHOW_DESKTOP);
			}
		});

		addHandler(DesktopMessages.SHOW_DESKTOP, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				User user = userService.getUser();
				Vendor vendor = user.getVendor();
				
				view.layoutDesktop(vendor);
				showEditorBanner();

				send(FileMessages.LAYOUT);

				view.showDesktop();

				send(DesktopMessages.DESKTOP_SHOWN);
			}
		});

		addHandler(DesktopMessages.DESKTOP_SHOWN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
                String editOrderId = Window.Location.getParameter("editOrderId");
                if (!Strings.isNullOrEmpty(editOrderId)) {
                    send(new OpenOrderMessage(Integer.parseInt(editOrderId)));
                } else {
                    send(FileMessages.SHOW_FOLDERS);
                }
			}
		});

		addHandler(OrderMessages.ORDER_SHOWN, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.enableProcessButton();
				view.enableCloseButton();
				view.enableDisposeButton();
			}
		});

		addHandler(OrderMessages.CLOSE_ORDER, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.disableProcessButton();
				view.disableCloseButton();
				view.disableDisposeButton();
			}
		});
		
		addHandler(FileMessages.DISPOSE, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.showDisposeProgress();
			}
		});

		addHandler(FileMessages.DISPOSE_RESULT, new MessageHandler<Message>() {
			@Override
			public void handle(Message message) {
				view.hideDisposeProgress();
			}
		});
	}

	private void showEditorBanner() {
		bannerService.getEditorBannerText(new XAsyncCallback<String>() {
			@Override
			public void onSuccess(String bannerText) {
				if (Strings.isNullOrEmpty(bannerText)) {
					return;
				}
				view.showBanner(bannerText);
			}
		});
	}
}
