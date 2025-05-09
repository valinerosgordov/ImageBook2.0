package ru.imagebook.client.admin2.view;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.admin2.ctl.BannersPresenter;


public interface BannersView extends IsWidget {

    void setPresenter(BannersPresenter presenter);

    void show();
}
