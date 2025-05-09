package ru.imagebook.client.admin2.view;

import com.google.gwt.user.client.ui.IsWidget;
import ru.imagebook.client.admin2.ctl.CodesImportPresenter;
import ru.imagebook.client.admin2.ctl.FeedbacksPresenter;

public interface FeedbacksView extends IsWidget {

    void setPresenter(FeedbacksPresenter presenter);

    void show();

}
