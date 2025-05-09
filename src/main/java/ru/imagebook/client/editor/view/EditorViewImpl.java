package ru.imagebook.client.editor.view;

import com.google.gwt.core.client.GWT;
import ru.imagebook.client.editor.ctl.EditorView;
import ru.imagebook.client.editor.ctl.file.DisposeMessage;
import ru.imagebook.client.editor.ctl.order.CloseOrderMessage;
import ru.imagebook.client.editor.ctl.order.OrderMessages;
import ru.imagebook.client.editor.ctl.order.ProcessOrderMessage;
import ru.imagebook.shared.model.Vendor;
import ru.minogin.core.client.browser.Browser;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.saasengine.client.service.auth.AuthService;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.Viewport;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EditorViewImpl extends View implements EditorView {
	private static final int MARGIN = 5;

	private final Widgets widgets;
	private final DesktopConstants constants;
	private final DesktopMessages messages;
	private final CommonConstants appConstants;
	private final AuthService authService;

	private Viewport viewport;
	private Button processButton;
	private Button closeButton;
	private Button disposeButton;
	private HTML bannerHTML;

	private MessageBox disposeProgressBox;

	@Inject
	public EditorViewImpl(Dispatcher dispatcher, Widgets widgets, DesktopConstants constants,
			CommonConstants appConstants, AuthService authService, DesktopMessages messages) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
		this.authService = authService;
		this.messages = messages;
	}

	@Override
	public void layoutDesktop(final Vendor vendor) {
		viewport = new Viewport();
		viewport.setLayout(new RowLayout());

		// app title and banner
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("app-title");

		Text text = new Text(messages.editorOfVendor(vendor.getName()));
		hPanel.add(text);

		bannerHTML = new HTML();
		bannerHTML.addStyleName("editor-banner-html");
		bannerHTML.setVisible(false);
		hPanel.add(bannerHTML);

		viewport.add(hPanel, new RowData(1, -1));

		ToolBar toolBar = new ToolBar();
		toolBar.add(new Button(constants.newOrderButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(OrderMessages.NEW_ORDER);
			}
		}));
		toolBar.add(new Button(constants.openOrderButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(OrderMessages.OPEN_ORDER_REQUEST);
			}
		}));

		processButton = new Button(constants.processOrderButton(),
				new SelectionListener<ButtonEvent>() {
					@Override
					public void componentSelected(ButtonEvent ce) {
						new ConfirmMessageBox(appConstants.warning(), constants.confirmProcess(),
								new Listener<BaseEvent>() {
									@Override
									public void handleEvent(BaseEvent be) {
										send(new ProcessOrderMessage());
									}
								});
					}
				});
		processButton.setEnabled(false);
		toolBar.add(processButton);

		closeButton = new Button(constants.closeOrderButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				new ConfirmMessageBox(appConstants.warning(), constants.confirmClose(),
						new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								send(new CloseOrderMessage());
							}
						});
			}
		});
		closeButton.setEnabled(false);
		toolBar.add(closeButton);

		toolBar.add(new SeparatorToolItem());

		disposeButton = new Button(constants.disposeButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				new ConfirmMessageBox(appConstants.warning(), constants.confirmDispose(),
						new Listener<BaseEvent>() {
							@Override
							public void handleEvent(BaseEvent be) {
								send(new DisposeMessage());
							}
						});
			}
		});
		disposeButton.setEnabled(false);
		toolBar.add(disposeButton);

		toolBar.add(new SeparatorToolItem());

		toolBar.add(new Button(constants.officeButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				String url = "http://" + vendor.getOfficeUrl() + "?sid=" + authService.getSessionId();
				Window.open(url, "_blank", "");
			}
		}));

        toolBar.add(new SeparatorToolItem());

        toolBar.add(new Button(constants.logoutButton(), new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                logout();
            }
        }));

		viewport.add(toolBar, new RowData(1, -1));

		LayoutContainer container = new LayoutContainer(new BorderLayout());

		LayoutContainer spreadContainer = new LayoutContainer(new AbsoluteLayout());
		spreadContainer.setBorders(true);
		spreadContainer.setScrollMode(Scroll.AUTO);
		BorderLayoutData data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(MARGIN, MARGIN, 0, MARGIN));
		container.add(spreadContainer, data);
		widgets.add(EditorWidgets.SPREAD_CONTAINER, spreadContainer);

		LayoutContainer westContainer = new LayoutContainer(new BorderLayout());

		ContentPanel foldersPanel = new ContentPanel(new FitLayout());
		widgets.add(EditorWidgets.FOLDERS_PANEL, foldersPanel);
		// foldersPanel.setHeading(constants.foldersPanel());
		foldersPanel.setHeaderVisible(false);
		data = new BorderLayoutData(LayoutRegion.NORTH);
		data.setMargins(new Margins(MARGIN, 0, 0, MARGIN));
		data.setSplit(true);
		westContainer.add(foldersPanel, data);

		ContentPanel previewPanel = new ContentPanel(new RowLayout());
		widgets.add(EditorWidgets.PREVIEW_PANEL, previewPanel);
		previewPanel.setScrollMode(Scroll.AUTOY);
		// previewPanel.setHeading(constants.imagesPanel());
		previewPanel.setHeaderVisible(false);
		data = new BorderLayoutData(LayoutRegion.CENTER);
		data.setMargins(new Margins(MARGIN, 0, 0, MARGIN));
		data.setSplit(true);
		westContainer.add(previewPanel, data);

		data = new BorderLayoutData(LayoutRegion.WEST);
		data.setSplit(true);
		container.add(westContainer, data);

		ContentPanel pagesPanel = new ContentPanel(new RowLayout(Orientation.HORIZONTAL));
		widgets.add(EditorWidgets.PAGES_PANEL, pagesPanel);
		pagesPanel.setHeaderVisible(false);
		pagesPanel.setScrollMode(Scroll.AUTO);

		toolBar = new ToolBar();
		toolBar.add(new LabelToolItem(""));
		pagesPanel.setTopComponent(toolBar);

		data = new BorderLayoutData(LayoutRegion.SOUTH, 145);
		data.setMargins(new Margins(MARGIN));
		container.add(pagesPanel, data);

		viewport.add(container, new RowData(1, 1));
	}

	@Override
	public void showDesktop() {
		RootPanel.get().add(viewport);
	}

	@Override
	public void enableProcessButton() {
		processButton.setEnabled(true);
	}

	@Override
	public void disableProcessButton() {
		processButton.setEnabled(false);
	}

	@Override
	public void enableCloseButton() {
		closeButton.setEnabled(true);
	}

	@Override
	public void disableCloseButton() {
		closeButton.setEnabled(false);
	}

	@Override
	public void alertIE() {
		MessageBox.alert(appConstants.error(), constants.ie(), null);
	}

	@Override
	public void enableDisposeButton() {
		disposeButton.setEnabled(true);
	}

	@Override
	public void disableDisposeButton() {
		disposeButton.setEnabled(false);
	}

	@Override
	public void showDisposeProgress() {
		disposeProgressBox = MessageBox.wait(constants.disposeProgressTitle(),
				constants.disposeProgressMessage(), constants.disposeProgressText());
	}

	@Override
	public void hideDisposeProgress() {
		disposeProgressBox.close();
	}

	@Override
	public void showBanner(String bannerText) {
		bannerHTML.setHTML(bannerText);
		bannerHTML.setVisible(true);
	}

    private void logout() {
        Browser.goTo(GWT.getHostPageBaseURL() + "logout");
    }
}
