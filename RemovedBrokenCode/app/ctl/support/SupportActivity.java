package ru.imagebook.client.app.ctl.support;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.service.SupportRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.support.SupportView;


public class SupportActivity extends AbstractActivity implements SupportPresenter {
    private final SupportRemoteServiceAsync supportService;
    private final SupportView view;

    @Inject
    public SupportActivity(SupportView view, SupportRemoteServiceAsync supportService) {
        this.view = view;
        view.setPresenter(this);
        this.supportService = supportService;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
    }

    @Override
    public void sendRequest(String subject, String text) {
        view.getSendButton().setEnabled(false);
        supportService.sendRequest(subject, text, new AsyncCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                view.notifyRequestSent();
                view.getSendButton().setEnabled(true);
            }
        });
    }
}
