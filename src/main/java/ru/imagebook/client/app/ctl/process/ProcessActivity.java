package ru.imagebook.client.app.ctl.process;

import java.util.List;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;

import ru.imagebook.client.app.service.ProcessRemoteServiceAsync;
import ru.imagebook.client.app.util.rpc.AsyncCallback;
import ru.imagebook.client.app.view.process.ProcessView;
import ru.imagebook.shared.model.Order;


public class ProcessActivity extends AbstractActivity implements ProcessPresenter {
    private final ProcessRemoteServiceAsync processService;
    private final ProcessView view;

    @Inject
    public ProcessActivity(ProcessView view, ProcessRemoteServiceAsync processService) {
        this.view = view;
        this.processService = processService;
    }

    @Override
    public void start(AcceptsOneWidget panel, EventBus eventBus) {
        panel.setWidget(view);
        loadOrders();
    }

    private void loadOrders() {
        processService.loadProcessingOrders(new AsyncCallback<List<Order<?>>>() {
            @Override
            public void onSuccess(List<Order<?>> processingOrders) {
                if (processingOrders.isEmpty()) {
                    view.showNoOrders();
                } else {
                    view.showProcessingOrders(processingOrders);
                }
            }
        });
    }
}
