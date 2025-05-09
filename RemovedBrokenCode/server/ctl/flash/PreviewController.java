package ru.imagebook.server.ctl.flash;

import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;

import ru.imagebook.server.ctl.RemoteSessionMessage;
import ru.imagebook.server.service.preview.PreviewService;
import ru.minogin.core.client.flow.Controller;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.MessageHandler;

public class PreviewController extends Controller {
    @Autowired
    private PreviewService previewService;

    @Autowired
    public PreviewController(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void registerHandlers() {
        addHandler(PreviewMessages.SHOW_PREVIEW, new MessageHandler<ShowPreviewMessage>() {
            @Override
            public void handle(ShowPreviewMessage message) {
                int userId = RemoteSessionMessage.getUserId(message);
                int orderId = message.getOrderId();
                Writer writer = message.getWriter();
                previewService.showOrderPreview(orderId, userId, writer);
            }
        });
    }
}
