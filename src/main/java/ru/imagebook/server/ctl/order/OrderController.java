package ru.imagebook.server.ctl.order;

import org.springframework.beans.factory.annotation.Autowired;
import ru.imagebook.client.admin.ctl.pricing.PricingMessages;
import ru.imagebook.server.ctl.ServerMessages;
import ru.imagebook.server.service.OrderService;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.MessageHandler;

public class OrderController extends Controller {
    @Autowired
    private OrderService service;

    @Autowired
    public OrderController(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void registerHandlers() {
        addHandler(ServerMessages.START, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                service.loadPricingData();
            }
        });

        addHandler(PricingMessages.PRICING_DATA_SAVED, new MessageHandler<Message>() {
            @Override
            public void handle(Message message) {
                service.loadPricingData();
            }
        });
    }
}
