package ru.imagebook.client.admin2.view;

import com.google.gwt.user.client.ui.IsWidget;
import ru.imagebook.client.admin2.ctl.RecommendationsPresenter;

public interface RecommendationsView extends IsWidget {
	void setPresenter(RecommendationsPresenter presenter);

    void show();

}
