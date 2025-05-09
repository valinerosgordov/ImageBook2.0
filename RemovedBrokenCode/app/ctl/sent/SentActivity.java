package ru.imagebook.client.app.ctl.sent;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.service.SentRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.sent.SentView;
import ru.imagebook.shared.model.Order;


public class SentActivity extends AbstractActivity implements SentPresenter {
    private final SentRemoteServiceAsync sentService;
    private final SentView view;

    @Inject
    public SentActivity(SentView view, SentRemoteServiceAsync sentService) {
        this.view = view;
        this.sentService = sentService;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        loadOrders();
    }

    private void loadOrders() {
        sentService.loadSentOrders(new AsyncCallback<List<Order<?>>>() {
            @Override
            public void onSuccess(List<Order<?>> sentOrders) {
                if (sentOrders.isEmpty()) {
                    view.showNoOrders();
                } else {
                    view.showSentOrders(sentOrders);
                }
            }
        });
    }
}
