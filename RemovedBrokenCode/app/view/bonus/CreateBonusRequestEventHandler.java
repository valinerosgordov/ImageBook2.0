package ru.imagebook.client.app.view.bonus;

import com.google.gwt.event.shared.EventHandler;


public interface CreateBonusRequestEventHandler extends EventHandler {
    void onBonusStatusRequest(CreateBonusRequestEvent createBonusRequestEvent);
}
