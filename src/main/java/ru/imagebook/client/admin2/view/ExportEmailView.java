package ru.imagebook.client.admin2.view;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.admin2.ctl.ExportEmailPresenter;


public interface ExportEmailView extends IsWidget {

    void setPresenter(ExportEmailPresenter presenter);

    void show();
}
