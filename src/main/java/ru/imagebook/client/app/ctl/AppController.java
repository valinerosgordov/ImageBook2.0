package ru.imagebook.client.app.ctl;

import com.google.common.base.Strings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

import ru.imagebook.client.app.ctl.order.OrderPlace;
import ru.imagebook.client.app.ctl.payment.BillsPlace;
import ru.imagebook.client.app.ctl.personal.PersonalPlace;
import ru.imagebook.client.app.ctl.process.ProcessPlace;
import ru.imagebook.client.app.ctl.register.RegisterActivity;
import ru.imagebook.client.app.ctl.register.RegisterActivityFactory;
import ru.imagebook.client.app.ctl.sent.SentPlace;
import ru.imagebook.client.app.ctl.support.SupportPlace;
import ru.imagebook.client.app.ctl.user.AppUserController;
import ru.imagebook.client.app.service.ActionRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.AppView;
import ru.imagebook.client.common.ctl.register.RegisterController;
import ru.imagebook.client.common.ctl.register.RegisterView;
import ru.imagebook.client.common.service.AuthData;
import ru.imagebook.client.common.service.AuthServiceAsync;
import ru.imagebook.client.common.service.BannerRemoteServiceAsync;
import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.VendorRemoteServiceAsync;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.client.common.util.YaMetrikaUtils;
import ru.imagebook.shared.model.Module;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.app.RequestStateResult;
import ru.minogin.core.client.flow.remoting.RemotingPostController;
import ru.minogin.core.client.gwt.ClientParametersReader;
import ru.saasengine.client.ctl.auth.AuthPreController;
import ru.saasengine.client.service.auth.AuthService;


public class AppController implements AppPresenter, PlaceChangeEvent.Handler {
    interface AppEventBinder extends EventBinder<AppController> {
    }

    private final AppView view;
    private final EventBus eventBus;
    private final AppEventBinder eventBinder = GWT.create(AppEventBinder.class);

    @Inject
    private PlaceController placeController;
    @Inject
    private PlaceHistoryHandler placeHistoryHandler;
    @Inject
    private ClientParametersReader clientParametersReader;
    @Inject
    private VendorService vendorService;
    @Inject
    private AuthServiceAsync authService;
    @Inject
    private UserService userService;
    @Inject
    private AuthService authService2;
    @Inject
    private RegisterController registerController;
    @Inject
    private AppUserController appUserController;
    @Inject
    private AuthPreController authPreController;
    @Inject
    private RemotingPostController remotingPostController;
    @Inject
    private RegisterView registerView;
    @Inject
    private BannerRemoteServiceAsync bannerService;
    @Inject
    private ActionRemoteServiceAsync actionService;
    @Inject
    private VendorRemoteServiceAsync vendorRemoteServiceAsync;
    @Inject
    private RegisterActivityFactory registerActivityFactory;

    @Inject
    public AppController(AppView view, EventBus eventBus) {
        this.eventBus = eventBus;
        this.view = view;
        view.setPresenter(this);
        eventBinder.bindEventHandlers(this, eventBus);
    }

    public void start() {
        if (clientParametersReader.hasParam("login")) {
            authPreController.registerHandlers();
            appUserController.registerHandlers();
            registerController.registerHandlers();
            remotingPostController.registerHandlers();
            registerView.showLinks(Module.App);
        } else {
            loadVendor();
        }

        // Add a Listener to the place change Event
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
    }

    private void loadVendor() {
        vendorRemoteServiceAsync.getVendor(new AsyncCallback<Vendor>() {
            @Override
            public void onSuccess(Vendor vendor) {
                vendorService.setVendor(vendor);
                view.showVendorInfo(vendor);

                if (clientParametersReader.hasParam("register")) {
                    showRegisterPage();
                } else {
                    RootPanel.get().add(view);
                    loadAuthData();
                }
            }
        });
    }

    private void showRegisterPage() {
        RegisterActivity registerActivity = registerActivityFactory.create();
        registerActivity.start(new AcceptsOneWidget() {
            @Override
            public void setWidget(IsWidget w) {
                if (w != null) {
                    RootPanel rootPanel = RootPanel.get();
                    rootPanel.clear();
                    rootPanel.add(w);
                }
            }
        }, null);
    }

    private void loadAuthData() {
        authService.auth(new AsyncCallback<AuthData>() {
            @Override
            public void onSuccess(AuthData data) {
                User user = data.getUser();

                if (!user.isRegistered()) {
                    Window.Location.assign("/register");
                    return;
                }

                userService.setUser(user);
                authService2.setSessionId(data.getSessionId());

                view.showUserInfo(user);
                showAppBanner();

                placeHistoryHandler.handleCurrentHistory();
                if (Strings.isNullOrEmpty(History.getToken())) {
                    History.newItem(NameTokens.ORDER);
                }
            }
        });
    }

    private void showAppBanner() {
        bannerService.getAppBannerText(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String bannerText) {
                if (Strings.isNullOrEmpty(bannerText)) {
                    return;
                }
                view.showBanner(bannerText);
            }
        });
    }

    @Override
    public void onOrderAnchorClicked() {
        placeController.goTo(new OrderPlace());
    }

    @Override
    public void onPaymentAnchorClicked() {
        placeController.goTo(new BillsPlace());
    }

    @Override
    public void onProcessOrdersAnchorClicked() {
        placeController.goTo(new ProcessPlace());
    }

    @Override
    public void onSentOrdersAnchorClicked() {
        placeController.goTo(new SentPlace());
    }

    @Override
    public void onProfileAnchorClicked() {
        placeController.goTo(new PersonalPlace());
    }

    @Override
    public void onEditorAnchorClicked() {
        String sessionId = authService2.getSessionId();
        String url;
        if (GWT.isProdMode())
            url = "http://editor.imagebook.ru?sid=" + sessionId;
        else
            url = "http://editor.test.imagebook.ru?sid=" + sessionId;
        Window.open(url, "_blank", null);
    }

    @Override
    public void onSupportAnchorClicked() {
        placeController.goTo(new SupportPlace());
    }

    @Override
    public void onBonusStatusAnchorClicked() {
        view.getBonusStatusAnchor().setEnabled(false);

        actionService.getRequestState(new AsyncCallback<Integer>() {
            @Override
            public void onSuccess(Integer state) {
                if (state == RequestStateResult.NEW) {
                    view.showBonusStatusRequestModalForm();
                } else if (state == RequestStateResult.PROCESSING) {
                    view.notifyBonusRequestProcessing();
                } else if (state == RequestStateResult.APPROVED) {
                    view.notifyBonusRequestApproved();
                } else if (state == RequestStateResult.ACTIVATED) {
                    view.notifyBonusRequestActivated();
                }
                view.getBonusStatusAnchor().setEnabled(true);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                view.getBonusStatusAnchor().setEnabled(true);
            }
        });
    }

    @Override
    public void createBonusStatusRequest(String request) {
        view.hideBonusStatusRequestModalForm();

        actionService.createBonusStatusRequest(request, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.notifyBonusStatusRequestSent();
                YaMetrikaUtils.statusRequestGoal();
            }
        });
    }

    @EventHandler
    public void onUserNameUpdated(UserNameUpdatedEvent event) {
        view.showUserInfo(event.getUser());
    }

    /**
     * Handle PlaceChangeEvent to update app navigation
     * @param event
     */
    @Override
    public void onPlaceChange(PlaceChangeEvent event) {
        view.updateNavActive(History.getToken());
    }
}