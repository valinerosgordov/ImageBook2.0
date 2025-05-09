package ru.imagebook.client.app.view.common;


public class PreviewModalDialog extends ModalDialog {
    public PreviewModalDialog(String dialogId) {
        super(dialogId);

        body.getElement().setAttribute("style", "padding: 0; background-color: #f0f0f0;");
        footer.remove(okButton);
    }
}
