package ru.minogin.undo.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import ru.minogin.undo.client.ctl.UndoPresenter;

public class UndoViewImpl implements UndoView {
	private UndoPresenter presenter;
	private HorizontalPanel panel;
	private Label label;

	private UndoConstants constants = GWT.create(UndoConstants.class);
	private Button undoButton;

	public UndoViewImpl() {
		Resources.INSTANCE.undoPanelCss().ensureInjected();
	}

	@Override
	public void setPresenter(UndoPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showUndoMessage(int count) {
		if (panel == null) {
			panel = new HorizontalPanel();
			panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			panel.addStyleName(Resources.INSTANCE.undoPanelCss().undoPanel());

			label = new Label();
			panel.add(label);
			undoButton = new Button(constants.undo(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.onUndo();
				}
			});
			panel.add(undoButton);
			Style style = panel.getElement().getStyle();
			style.setPosition(Position.ABSOLUTE);
			panel.setVisible(false);
			RootPanel.get().add(panel);
		}

		label.setText(constants.changesSaved() + " (" + count + ")");
		panel.setVisible(true);
		int width = panel.getOffsetWidth();
		RootPanel contentPanel = RootPanel.get("content");
		Style style = panel.getElement().getStyle();
		style.setLeft(
				contentPanel.getAbsoluteLeft() + contentPanel.getOffsetWidth() - width,
				Unit.PX);
		style.setTop(contentPanel.getAbsoluteTop(), Unit.PX);
	}

	@Override
	public void hideUndoPanel() {
		if (panel != null)
			panel.setVisible(false);
	}

	@Override
	public void enableUndoButton() {
		undoButton.setEnabled(true);
	}

	@Override
	public void disableUndoButton() {
		undoButton.setEnabled(false);
	}
}
