package ru.imagebook.client.common.view.order;

import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.safehtml.shared.SafeHtml;

/**

 * @since 16.12.2014
 */
public interface PreviewConstants extends Messages {
    String previewTitle(String number);

	String showPreviewFailed();

    String close();

    String flashDiv(int flashWidth, int flashHeight);
}
