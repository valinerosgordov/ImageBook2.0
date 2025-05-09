package ru.imagebook.client.app.view;

import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;


public interface AppMessages extends Messages {
    SafeHtml star();

    String mailTo(String email);

    SafeHtml copyright(int year, String companyName);

    String userLevel();
}
