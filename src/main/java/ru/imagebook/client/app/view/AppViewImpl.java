package ru.imagebook.client.app.view;

import java.util.Date;

import com.google.common.base.Strings;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.app.ctl.AppPresenter;
import ru.imagebook.client.app.ctl.NameTokens;
import ru.imagebook.client.app.view.bonus.BonusRequestConstants;
import ru.imagebook.client.app.view.bonus.BonusStatusRequestModalForm;
import ru.imagebook.client.app.view.bonus.CreateBonusRequestEvent;
import ru.imagebook.client.app.view.common.Anchor;
import ru.imagebook.client.app.view.common.Notify;
import ru.imagebook.shared.model.User;
import ru.imagebook.shared.model.Vendor;
import ru.imagebook.shared.model.VendorType;
import ru.minogin.core.client.constants.CommonConstants;


@Singleton
public class AppViewImpl extends Composite implements AppView {
    interface AppLayoutUiBinder extends UiBinder<Widget, AppViewImpl> {
    }
    private static AppLayoutUiBinder uiBinder = GWT.create(AppLayoutUiBinder.class);

    @UiField
    HTMLPanel headerContainer;
    @UiField
    DivElement navBarDiv;
    @UiField
    HTMLPanel logoPanel;
    @UiField
    InlineHTML userInfoHtml;
    @UiField
    HorizontalPanel userBonusPanel;
    @UiField
    HTMLPanel bannerPanel;
    @UiField
    HTML bannerHtml;
    @UiField
    InlineHTML vendorContactsHtml;
    @UiField
    Anchor logoAnchor;
    @UiField
    UListElement navUList;
    @UiField
    Anchor orderAnchor;
    @UiField
    Anchor paymentAnchor;
    @UiField
    Anchor processOrdersAnchor;
    @UiField
    Anchor sentOrdersAnchor;
    @UiField
    Anchor profileAnchor;
    @UiField
    Anchor editorAnchor;
    @UiField
    Anchor supportAnchor;
    @UiField
    SimplePanel contentContainer;
    @UiField
    ParagraphElement copyrightParagraph;
    @UiField
    BonusStatusRequestModalForm bonusStatusRequestModalForm;

    private final AppMessages appMessages;
    private final BonusRequestConstants bonusRequestConstants;
    private final CommonConstants commonConstants;

    private AppPresenter presenter;
    private Anchor bonusStatusAnchor;

    @Inject
    public AppViewImpl(ActivityManager activityManager, AppMessages appMessages, CommonConstants commonConstants,
                       BonusRequestConstants bonusRequestConstants) {
        this.appMessages = appMessages;
        this.bonusRequestConstants = bonusRequestConstants;
        this.commonConstants = commonConstants;

        Widget ui = uiBinder.createAndBindUi(this);
        initWidget(ui);
        activityManager.setDisplay(this.contentContainer);

        // TODO add affix effect
//        ui.addAttachHandler(new AttachEvent.Handler() {
//            @Override
//            public void onAttachOrDetach(AttachEvent event) {
//                System.out.println("Header height = " + headerContainer.getOffsetHeight());
//                Affix.affix(navBarDiv, headerContainer.getOffsetHeight() * 3);
//            }
//        });

//        Window.addResizeHandler(new ResizeHandler() {
//            @Override
//            public void onResize(ResizeEvent event) {
//                System.out.println("Header height = " + headerContainer.getOffsetHeight());
//                Affix.affix(navBarDiv, headerContainer.getOffsetHeight() * 2);
//            }
//        });
    }

    @Override
    public void setPresenter(AppPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showVendorInfo(Vendor vendor) {
        if (vendor.getType() == VendorType.IMAGEBOOK) {
            Anchor imagebookLogoAnchor = new Anchor();
            imagebookLogoAnchor.setStyleName("logo");
            imagebookLogoAnchor.setHref(GWT.getHostPageBaseURL());
            logoPanel.add(imagebookLogoAnchor);
        } else {
            logoPanel.add(new HTMLPanel("h3", vendor.getName()));
        }
        showVendorContacts(vendor);
        showCopyright(vendor);
    }

    private void showVendorContacts(Vendor vendor) {
        String contacts = "";
        if (vendor.getPhone() != null) {
            contacts += vendor.getPhone() + "<br/>";
        }
        contacts += appMessages.mailTo(vendor.getEmail());
        vendorContactsHtml.setHTML(contacts);
    }

    private void showCopyright(Vendor vendor) {
        @SuppressWarnings("deprecation")
        int year = 1900 + new Date().getYear();
        copyrightParagraph.setInnerHTML(appMessages.copyright(year, vendor.getCompanyName()).asString());

        if (vendor.getType() == VendorType.IMAGEBOOK) {
            logoAnchor.setVisible(true);
        }
    }

    @Override
    public void showUserInfo(User user) {
        showUserName(user);
        showUserBonusLevel(user);
    }

    private void showUserName(User user) {
        String userName = user.getFullName();
        if (Strings.isNullOrEmpty(userName)) {
            userName = user.getUserName();
        } else {
            userName += "<br/>" + "(" + user.getUserName() + ")";
        }
        userInfoHtml.setHTML(userName);
    }

    private void showUserBonusLevel(User user) {
        userBonusPanel.clear();

        int level = user.getLevel();

        String levelText = appMessages.userLevel() + ": ";
        levelText += (level > 0) ? level : commonConstants.no();

        Vendor vendor = user.getVendor();
        if (vendor.getType() == VendorType.IMAGEBOOK) {
            bonusStatusAnchor = new Anchor(levelText);
            bonusStatusAnchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    presenter.onBonusStatusAnchorClicked();
                }
            });
            userBonusPanel.add(bonusStatusAnchor);
        } else {
            userBonusPanel.add(new HTML(levelText));
        }

        if (level > 0) {
            userBonusPanel.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
            for (int i = 0; i < level; i++) {
                userBonusPanel.add(new HTML(appMessages.star()));
            }
        }
    }

    @Override
    public Anchor getBonusStatusAnchor() {
        return bonusStatusAnchor;
    }

    @Override
    public void showBanner(String bannerText) {
        bannerHtml.setHTML(bannerText);
        bannerPanel.setVisible(true);
    }

    @UiHandler("orderAnchor")
    public void onOrderAnchorClicked(ClickEvent event) {
        presenter.onOrderAnchorClicked();
    }

    @UiHandler("paymentAnchor")
    public void onPaymentAnchorClicked(ClickEvent event) {
        presenter.onPaymentAnchorClicked();
    }

    @UiHandler("processOrdersAnchor")
    public void onProcessOrdersAnchorClicked(ClickEvent event) {
        presenter.onProcessOrdersAnchorClicked();
    }

    @UiHandler("sentOrdersAnchor")
    public void onSentOrdersAnchorClicked(ClickEvent event) {
        presenter.onSentOrdersAnchorClicked();
    }

    @UiHandler("profileAnchor")
    public void onProfileAnchorClicked(ClickEvent event) {
        presenter.onProfileAnchorClicked();
    }

    @UiHandler("editorAnchor")
    public void onEditorAnchorClicked(ClickEvent event) {
        presenter.onEditorAnchorClicked();
    }

    @UiHandler("supportAnchor")
    public void onSupportAnchorClicked(ClickEvent event) {
        presenter.onSupportAnchorClicked();
    }

    @Override
    public void updateNavActive(String token) {
        if (NameTokens.ORDER.equals(token)) {
            updateNavActiveUListElement(orderAnchor);
        } else if (NameTokens.PAYMENT.equals(token)) {
            updateNavActiveUListElement(paymentAnchor);
        } else if (NameTokens.PROCESS.equals(token)) {
            updateNavActiveUListElement(processOrdersAnchor);
        } else if (NameTokens.SENT.equals(token)) {
            updateNavActiveUListElement(sentOrdersAnchor);
        } else if (NameTokens.PERSONAL.equals(token)) {
            updateNavActiveUListElement(profileAnchor);
        } else if (NameTokens.SUPPORT.equals(token)) {
            updateNavActiveUListElement(supportAnchor);
        }
    }

    private void updateNavActiveUListElement(Anchor activeAnchor) {
        for (int i = 0; i < navUList.getChildCount(); i++) {
            Node child = navUList.getChild(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child instanceof UListElement) {
                ((UListElement) child).removeClassName("active");
            }
        }
        Element el = activeAnchor.getElement().getParentElement();
        if (el instanceof UListElement) {
            el.addClassName("active");
        }
    }

    @Override
    public void showBonusStatusRequestModalForm() {
        bonusStatusRequestModalForm.show();
    }

    @Override
    public void hideBonusStatusRequestModalForm() {
        bonusStatusRequestModalForm.hide();
    }

    @UiHandler("bonusStatusRequestModalForm")
    public void onBonusStatusRequest(CreateBonusRequestEvent event) {
        presenter.createBonusStatusRequest(event.getRequestValue());
    }

    @Override
    public void notifyBonusRequestProcessing() {
        Notify.info(bonusRequestConstants.requestProcessing());
    }

    @Override
    public void notifyBonusRequestApproved() {
        Notify.info(bonusRequestConstants.requestApproved());
    }

    @Override
    public void notifyBonusRequestActivated() {
        Notify.info(bonusRequestConstants.requestActivated());
    }

    @Override
    public void notifyBonusStatusRequestSent() {
        Notify.info(bonusRequestConstants.requestSent());
    }
}
