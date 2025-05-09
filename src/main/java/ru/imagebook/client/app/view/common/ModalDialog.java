package ru.imagebook.client.app.view.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;


public class ModalDialog extends Composite {
    interface ModalDialogWidgetUiBinder extends UiBinder<Widget, ModalDialog> {
    }

    @UiField
    FlowPanel modal;
    @UiField
    FlowPanel dialog;
    @UiField
    FlowPanel header;
    @UiField
    FlowPanel body;
    @UiField
    FlowPanel footer;
    @UiField
    Button cancelButton;
    @UiField
    Button okButton;

    private static ModalDialogWidgetUiBinder uiBinder = GWT.create(ModalDialogWidgetUiBinder.class);

    private String dialogId;

    @UiConstructor
    public ModalDialog(String dialogId) {
        initWidget(uiBinder.createAndBindUi(this));
        this.dialogId = dialogId;

        modal.getElement().setAttribute("id", dialogId);
        modal.getElement().setAttribute("role", "dialog");
        modal.getElement().setAttribute("aria-hidden", "true");
        modal.getElement().setAttribute("tabindex", "-1");
        modal.getElement().setAttribute("style", "display: none");
    }

    @UiChild(tagname = "body")
    public void addBody(Widget widget) {
        body.add(widget);
    }

    @UiChild(tagname = "header")
    public void addHeader(Widget widget) {
        header.add(widget);
    }

    public void setModalWidth(String width) {
        dialog.setWidth(width);
    }

    public void setModalHeight(String height) {
        body.setHeight(height);
    }

    public void setCancelButtonTitle(String title) {
        cancelButton.setText(title);
    }

    public void setOkButtonTitle(String title) {
        okButton.setText(title);
    }

    public void setCancelButtonClickHandler(ClickHandler handler) {
        cancelButton.addClickHandler(handler);
    }

    public void setOkButtonClickHandler(ClickHandler handler) {
        okButton.addClickHandler(handler);
    }

    public void setOkButtonColor(String color) {
        okButton.setStyleName(color);
    }

    @Deprecated
    public void setCancelButtonStyle(String styleString) {
        cancelButton.getElement().setAttribute("style", styleString);
    }

    public void setOkButtonEnabled(boolean enabled) {
        okButton.setEnabled(enabled);
    }

    public void show() {
        modelJs("show", dialogId);
    }

    public void hide() {
        modelJs("hide", dialogId);
    }

    public static native void modelJs(String param, String dialogId) /*-{
        $wnd.$('#' + dialogId).modal(param);
    }-*/;
}
