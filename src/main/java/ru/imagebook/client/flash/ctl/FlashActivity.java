package ru.imagebook.client.flash.ctl;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.common.service.UserService;
import ru.imagebook.client.common.service.security.SecurityService;
import ru.imagebook.client.flash.service.FlashData;
import ru.imagebook.client.flash.service.FlashServiceAsync;
import ru.imagebook.client.flash.service.Flashes;
import ru.imagebook.client.flash.view.FlashPresenter;
import ru.imagebook.client.flash.view.FlashView;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Role;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.flash.Flash;
import ru.imagebook.shared.service.admin.flash.FlashExistsException;
import ru.imagebook.shared.service.admin.flash.TooManyFlashesException;
import ru.minogin.core.client.app.failure.XAsyncCallback;
import ru.minogin.core.client.gwt.ClientParametersReader;

@Singleton
public class FlashActivity extends AbstractActivity implements FlashPresenter {
	private static final int DEFAULT_WIDTH = 600;

	@Inject
	private FlashServiceAsync service;
	@Inject
	private FlashView view;
	@Inject
	private UserService userService;
	@Inject
	private SecurityService securityService;
	@Inject
	private ClientParametersReader clientParametersReader;

	private Order<?> order;
	private Flash flash;

	@Inject
	public FlashActivity(FlashView view) {
		view.setPresenter(this);
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		showAll();
	}

	private void showAll() {
		Integer publishCode = clientParametersReader.getParam("code");
		if (publishCode == null) {
            return;
        }

        service.loadData(publishCode, new XAsyncCallback<FlashData>() {
            @Override
            public void onSuccess(FlashData data) {
                order = data.getOrder();
                User user = data.getUser();
                userService.setUser(user);
                if (securityService.hasRole(Role.OPERATOR) || order.getUser().equals(user)) {
                    view.showPublishPanel(order);
                    showFlashes();
                }
            }
        });
	}

	@Override
	public void codeButtonClicked() {
		view.showParamForm(Flashes.MIN_FLASH_WIDTH, Flashes.MAX_FLASH_WIDTH);
		view.setFlashWidth(DEFAULT_WIDTH);
	}

	@Override
	public void generateButtonClicked() {
		int width = DEFAULT_WIDTH;
		try {
			width = view.getFlashWidth();
		} catch (InvalidValueException e) {
			view.alertInvalidValue();
		}

		if (width < Flashes.MIN_FLASH_WIDTH || width > Flashes.MAX_FLASH_WIDTH) {
			view.alertWrongValue();
			return;
		}

		view.hideParamForm();
		view.showProgress();
		service.generateFlash(order.getId(), width, new XAsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				view.hideProgress();
				showFlashes();
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof FlashExistsException) {
					view.hideProgress();
					view.alertFlashExists();
				} else if (caught instanceof TooManyFlashesException) {
					view.hideProgress();
					view.alertTooManyFlashes(Flashes.MAX_FLASHES);
				} else {
                    super.onFailure(caught);
                }
			}
		});
	}

	private void showFlashes() {
		service.loadFlashes(order.getId(), new XAsyncCallback<List<Flash>>() {
			@Override
			public void onSuccess(List<Flash> flashes) {
				view.showFlashes(flashes);
			}
		});
	}

	@Override
	public void deleteFlashButtonClicked(Flash flash) {
		this.flash = flash;
		view.confirmFlashDeletion();
	}

	@Override
	public void flashDeletionConfirmed() {
		service.deleteFlash(order.getId(), flash.getWidth(), new XAsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                showFlashes();
            }
        });
	}
}
