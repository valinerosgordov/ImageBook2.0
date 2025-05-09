package ru.imagebook.client.flash.view;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.flash.ctl.InvalidValueException;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.flash.Flash;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.gwt.ui.ConfirmDialogBox;
import ru.minogin.core.client.gwt.ui.MessageBox;

@Singleton
public class FlashViewImpl implements FlashView {
	@Inject
	private FlashConstants constants;
	@Inject
	private CommonConstants commonConstants;
	@Inject
	private FlashMessages messages;

	private FlashPresenter presenter;

	private TextBox widthField;
	private DialogBox paramForm;
	private DialogBox progressBox;
	private VerticalPanel flashesPanel;

	@Override
	public void setPresenter(FlashPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showPublishPanel(Order<?> order) {
		RootPanel rootPanel = RootPanel.get("app");
		rootPanel.clear();

        VerticalPanel panel = new VerticalPanel();

        flashesPanel = new VerticalPanel();
        panel.add(flashesPanel);

        Button codeButton = new Button(constants.codeButton(), new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                presenter.codeButtonClicked();
            }
        });
        codeButton.addStyleName("code-button");
        codeButton.addStyleName("blue");
        panel.add(codeButton);

        rootPanel.add(panel);
	}

	@Override
	public void showParamForm(int minWidth, int maxWidth) {
		paramForm = new DialogBox(false, true);
		paramForm.setText(constants.paramFormTitle());

		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("flash-code-panel");
		panel.setSize("300px", "80px");

		panel.add(new HTML(messages.widthInfo(minWidth, maxWidth)));

		HorizontalPanel hPanel = new HorizontalPanel();

		hPanel.add(new HTML(constants.widthField()));

		widthField = new TextBox();
		hPanel.add(widthField);

		panel.add(hPanel);

		hPanel = new HorizontalPanel();
		hPanel.add(new Button(constants.generateButton(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				presenter.generateButtonClicked();
			}
		}));
		hPanel.add(new Button(commonConstants.cancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				paramForm.hide();
			}
		}));
		panel.add(hPanel);

		paramForm.setWidget(panel);

		paramForm.center();
	}

	@Override
	public void setFlashWidth(int width) {
		widthField.setValue(width + "");
	}

	@Override
	public int getFlashWidth() {
		try {
			return new Integer(widthField.getValue());
		} catch (NumberFormatException e) {
			throw new InvalidValueException();
		}
	}

	@Override
	public void alertInvalidValue() {
		new MessageBox(commonConstants.error(), constants.invalidValue(), commonConstants).show();
	}

	@Override
	public void alertWrongValue() {
		new MessageBox(commonConstants.error(), constants.wrongValue(), commonConstants).show();
	}

	@Override
	public void hideParamForm() {
		paramForm.hide();
	}

	@Override
	public void showProgress() {
		progressBox = new DialogBox(false, true);
		progressBox.setText(constants.progressTitle());
		VerticalPanel panel = new VerticalPanel();
		panel.setSize("400px", "100px");
		panel.add(new HTML(constants.progressText()));
		progressBox.setWidget(panel);
		progressBox.center();
	}

	@Override
	public void hideProgress() {
		progressBox.hide();
	}

	@Override
	public void showFlashes(List<Flash> flashes) {
		flashesPanel.clear();

		if (flashes.isEmpty())
			return;

		FlexTable table = new FlexTable();
		table.addStyleName("flashes-table");
		table.setText(0, 0, constants.size());
		table.setText(0, 1, constants.code());
		table.getRowFormatter().addStyleName(0, "flashes-table-title");
		int y = 1;
		for (final Flash flash : flashes) {
			table.setText(y, 0, flash.getWidth() + " x " + flash.getHeight());

			TextArea codeArea = new TextArea();
			codeArea.setSize("300px", "100px");
			codeArea.setText(flash.getCode());
			table.setWidget(y, 1, codeArea);

			table.setWidget(y, 2, new Button(constants.deleteFlashButton(), new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					presenter.deleteFlashButtonClicked(flash);
				}
			}));

			table.getRowFormatter().addStyleName(y, "flashes-table-cell");

			y++;
		}
		flashesPanel.add(table);
	}

	@Override
	public void confirmFlashDeletion() {
        new ConfirmDialogBox(commonConstants.warning(), constants.confirmDeleteFlash(), commonConstants,
            new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    presenter.flashDeletionConfirmed();
                }
            }).show();
	}

	@Override
	public void alertFlashExists() {
		new MessageBox(commonConstants.error(), constants.flashExists(), commonConstants).show();
	}

	@Override
	public void alertTooManyFlashes(int maxFlashes) {
		new MessageBox(commonConstants.error(), messages.tooManyFlashes(maxFlashes), commonConstants).show();
	}
}
