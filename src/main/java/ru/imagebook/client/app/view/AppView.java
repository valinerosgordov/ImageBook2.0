package ru.imagebook.client.app.view;

import com.google.gwt.user.client.ui.IsWidget;

import ru.imagebook.client.app.ctl.AppPresenter;
import ru.imagebook.client.app.view.common.Anchor;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;


public interface AppView extends IsWidget {
    void setPresenter(AppPresenter presenter);

    void showVendorInfo(Vendor vendor);

    void showUserInfo(User user);

    Anchor getBonusStatusAnchor();

    void showBanner(String bannerText);

    void updateNavActive(String token);

    void showBonusStatusRequestModalForm();

    void hideBonusStatusRequestModalForm();

    void notifyBonusRequestProcessing();

    void notifyBonusRequestApproved();

    void notifyBonusRequestActivated();

    void notifyBonusStatusRequestSent();
}
