package ru.imagebook.client.app.view.personal;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractParamTable extends FlexTable {
    private final Anchor addItemAnchor = new Anchor(true);

    public AbstractParamTable() {
        setStyleName("table param-table");
        addItemAnchor.getElement().setAttribute("role", "button");
    }

    protected final void setAddItemAnchorText(String text) {
        addItemAnchor.setText(text);
    }

    protected final void initTable() {
        removeAllRows();
        showAddItemAnchor();
    }

    protected final void showAddItemAnchor() {
        int row = getLastRowIndex();
        setWidget(row, 0, addItemAnchor);
        getFlexCellFormatter().setColSpan(row, 0, 2);
        getFlexCellFormatter().setStyleName(getLastRowIndex(), 0, "param-form-td");
        addItemAnchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                IsWidget form = showAddItemForm();
                setWidget(getLastRowIndex(), 0, form);
            }
        });
    }

    private int getLastRowIndex() {
        return getRowCount() == 0 ? getRowCount() : getRowCount() - 1;
    }

    public abstract IsWidget showAddItemForm();

    protected final void addItem(Object value, String deleteItemButtonLabel, ClickHandler deleteItemClickHandler) {
        int newRow = insertRow(getRowCount() - 1);
        if (value instanceof Widget) {
            setWidget(newRow, 0, (Widget) value);
        } else {
            setText(newRow, 0, (String) value);
        }

        Button deleteItemButton = new Button(deleteItemButtonLabel);
        deleteItemButton.setStyleName("btn btn-danger btn-xs");
        deleteItemButton.addClickHandler(deleteItemClickHandler);
        setWidget(newRow, 1, deleteItemButton);
        getFlexCellFormatter().setStyleName(newRow, 1, "delete-param-btn-td");
    }
}
