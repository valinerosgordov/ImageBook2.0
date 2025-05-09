package ru.imagebook.client.app.view.support;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.support.SupportPresenter;


public interface SupportView extends IsWidget {
    void setPresenter(SupportPresenter presenter);

    Button getSendButton();

    void notifyRequestSent();
}
