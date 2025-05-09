package ru.imagebook.client.app.ctl.register;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.service.LoginRemoteService;
import ru.imagebook.client.app.service.LoginRemoteServiceAsync;
import ru.imagebook.client.app.service.OrderRemoteService;
import ru.imagebook.client.app.service.OrderRemoteServiceAsync;
import ru.imagebook.client.app.service.RegisterRemoteService;
import ru.imagebook.client.app.service.RegisterRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.register.RegisterView;
import ru.imagebook.client.common.service.vendor.VendorService;
import ru.imagebook.client.common.util.YaMetrikaUtils;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.service.app.UserCaptchaIsInvalid;
import ru.imagebook.shared.service.app.UserExistsException;
import ru.minogin.core.client.format.EmailFormat;
import ru.saasengine.client.service.auth.AuthError;

/**
 * Extracted from Pickbook sources
 *

 * @since 08.12.2014
 */
@Singleton
public class RegisterActivity extends AbstractActivity implements RegisterPresenter {
    public static final int MIN_PASSWORD_LENGTH = 6;

    private final RegisterRemoteServiceAsync registerService = GWT.create(RegisterRemoteService.class);
    private final LoginRemoteServiceAsync loginService = GWT.create(LoginRemoteService.class);
    private final OrderRemoteServiceAsync orderService = GWT.create(OrderRemoteService.class);

    private final RegisterView view;
    private final VendorService vendorService;

    @Inject
    public RegisterActivity(RegisterView view, VendorService vendorService) {
        this.view = view;
        view.setPresenter(this);
        this.vendorService = vendorService;
    }

    @Override
    public void start(final AcceptsOneWidget panel, EventBus eventBus) {
        registerService.isRegistered(new AsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean registered) {
                if (registered) {
                    finish();
                } else {
                    panel.setWidget(view);

                    Vendor vendor = vendorService.getVendor();
                    view.setServiceName(vendor.getName());
                }
            }
        });
    }

    @Override
    public void onRegister(final String email, final String password, final String captcha) {
        if (email.isEmpty()) {
            view.alertRegEmailEmpty();
            return;
        }

        if (!email.matches(EmailFormat.EMAIL)) {
            view.alertRegEmailWrong();
            return;
        }

        if (password.isEmpty()) {
            view.alertRegPasswordEmpty();
            return;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            view.alertPasswordTooShort();
            return;
        }

        view.clearRegisterNotification();

        registerService.attachEmailAndPassword(email, password, captcha, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                finish();
                YaMetrikaUtils.lkRegistractionGoal();
            }

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof UserExistsException) {
                    loginAndMoveOrders(email, password);
                } else if (caught instanceof UserCaptchaIsInvalid) {
                    view.alertCaptchaIsInvalid();
                } else {
                    super.onFailure(caught);
                }
            }
        });
    }

    private void loginAndMoveOrders(final String email, final String password) {
        registerService.getAnonymousUserName(new AsyncCallback<String>() {
            @Override
            public void onSuccess(final String anonymousUserName) {
                loginService.login(email, password, new AsyncCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        if (anonymousUserName != null) {
                            view.showProgress();
                            moveOrders(anonymousUserName);
                        } else {
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof AuthError) {
                            view.alertAuthError();
                        } else {
                            super.onFailure(caught);
                        }
                    }
                });
            }
        });
    }

    private void moveOrders(String anonymousUserName) {
        orderService.moveAnonymousOrders(anonymousUserName, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                finish();
            }
        });
    }

    private void finish() {
        view.goTo("/");
    }
}
