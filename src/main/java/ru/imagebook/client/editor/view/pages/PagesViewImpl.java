package ru.imagebook.client.editor.view.pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.gwtgraphics.client.DrawingArea;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteData;
import com.extjs.gxt.ui.client.widget.layout.AbsoluteLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Event;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.imagebook.client.editor.ctl.file.CancelShowNotificationMessage;
import ru.imagebook.client.editor.ctl.pages.ChangePageCountMessage;
import ru.imagebook.client.editor.ctl.pages.ChangePageTypeOnIndividualMessage;
import ru.imagebook.client.editor.ctl.pages.ClearSpreadMessage;
import ru.imagebook.client.editor.ctl.pages.PagesMessages;
import ru.imagebook.client.editor.ctl.pages.PagesView;
import ru.imagebook.client.editor.ctl.pages.SelectLayoutMessage;
import ru.imagebook.client.editor.ctl.pages.SelectPageMessage;
import ru.imagebook.client.editor.view.EditorWidgets;
import ru.imagebook.client.editor.view.spread.PageRenderer;
import ru.imagebook.shared.model.Album;
import ru.imagebook.shared.model.AlbumOrder;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.editor.Layout;
import ru.imagebook.shared.model.editor.NotificationType;
import ru.imagebook.shared.model.editor.Page;
import ru.imagebook.shared.model.editor.PageType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;
import ru.minogin.core.client.gxt.form.XFormPanel;

@Singleton
public class PagesViewImpl extends View implements PagesView {
	private static final float HEIGHT_PX = 70;
	private static final float MARGIN_PX = 10;

	private final Widgets widgets;
	private SelectField<Layout> layoutsField;
	private final PageConstants constants;
	private final CommonConstants appConstants;
	private Button btn;

	private List<DrawingArea> canvases = new ArrayList<DrawingArea>();
	private List<LayoutContainer> containers = new ArrayList<LayoutContainer>();
	private float k;
	private int layoutNumber = 0;
	private Window pageCountWindow, showNotificationWindow;

	@Inject
	public PagesViewImpl(Dispatcher dispatcher, Widgets widgets, PageConstants constants,
			CommonConstants appConstants) {
		super(dispatcher);

		this.widgets = widgets;
		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showOrder(Order<?> order) {
		ContentPanel pagesPanel = widgets.get(EditorWidgets.PAGES_PANEL);
		pagesPanel.removeAll();
		canvases.clear();
		containers.clear();

		Album album = (Album) order.getProduct();
		Layout layout;
		if (order.isPackaged()) {
			layout = order.getLayouts().get(layoutNumber);
		} else {
			layout = order.getLayout();
		}
		final List<Page> pages = layout.getPages();

		float maxHeight = 0;
		for (Page page : pages) {
			if (page.getHeight() > maxHeight)
				maxHeight = page.getHeight();
		}
		k = HEIGHT_PX / maxHeight;

		int p = 1;
		for (int i = 0; i < pages.size(); i++) {
			Page page = pages.get(i);

			boolean isFirstPage = i == 0;
			boolean isLastPage = i == pages.size() - 1;

			float pageWidth = page.getWidth();
			float pageHeight = page.getHeight();
			int w = (int) (pageWidth * k + 2 * MARGIN_PX);
			int h = (int) (pageHeight * k + 3 * MARGIN_PX);

			final LayoutContainer container = new LayoutContainer(new AbsoluteLayout());
			container.setSize(w, h);
			container.addStyleName("editor-spread-container");

			container.sinkEvents(Event.ONCLICK);
			final int n = i;
			container.addListener(Events.OnClick, new Listener<BaseEvent>() {
				@Override
				public void handleEvent(BaseEvent be) {
					send(new SelectPageMessage(n, layoutNumber));
				}
			});

			containers.add(container);

			w = (int) (pageWidth * k);
			h = (int) (pageHeight * k);
			DrawingArea canvas = new DrawingArea(w, h);
			canvases.add(canvas);
			container.add(canvas, new AbsoluteData((int) MARGIN_PX, (int) MARGIN_PX));

			if (album.isSeparateCover() && isFirstPage) {
				final AbsoluteData ad = new AbsoluteData((int) MARGIN_PX + w / 2 - 30, (int) MARGIN_PX + h + 4);
				Html html = new Html(constants.cover() + "<p> (" + (page.isCommon() ? "Общ." : "Инд.") + ") </p>");
				html.addStyleName("page-number");
				container.add(html, ad);
			} else {
				if (page.getType() == PageType.NORMAL) {
					Html html = new Html(p + " <p>(" + (page.isCommon() ? "Общ." : "Инд.") + ")</p> ");
					html.addStyleName("page-number");
					final AbsoluteData ad = new AbsoluteData((int) MARGIN_PX + w / 2 - 5, (int) MARGIN_PX + h + 4);
					container.add(html, ad);

					p++;
				} else if (page.isSpreadOrFlyLeafPage()) {
				    boolean isFirstFlyLeaf = page.isFlyLeaf() && (!isLastPage || pages.size() < 3);
					Html html = new Html(getSpreadPageLabel(p, isFirstFlyLeaf, page.isCommon()));
					html.addStyleName("page-number");
					final AbsoluteData ad = new AbsoluteData((int) MARGIN_PX + w / 4 - 5, (int) MARGIN_PX + h + 4);
					container.add(html, ad);

					if (!page.isFlyLeaf()) {
						p++;
					}

                    boolean isLastFlyLeaf = page.isFlyLeaf() && isLastPage;
					html = new Html(getSpreadPageLabel(p, isLastFlyLeaf, page.isCommon()));
					html.addStyleName("page-number");
					final AbsoluteData ad2 =new AbsoluteData((int) MARGIN_PX + 3 * w / 4 - 5, (int) MARGIN_PX + h + 4);
					container.add(html, ad2);

					p++;
				}
			}

			// int i = pages.indexOf(page);
			// String name;
			// if (album.isSeparateCover() && i == 0)
			// name = constants.cover();
			// else {
			// name = (i + 1) + "";
			// }
			// Html html = new Html(name);
			// html.addStyleName("page-number");
			// container.add(html, new AbsoluteData((int) MARGIN_PX + w / 2, (int)
			// MARGIN_PX + h + 4));

			pagesPanel.add(container);
		}

		pagesPanel.layout();
	}

	private String getSpreadPageLabel(int pageNumber, boolean isFlyLeaf, boolean isCommon) {
		String text = " <p>(" + (isCommon ? "Общ." : "Инд.") + ")</p> ";
		return isFlyLeaf ? "форзац" + text : pageNumber + text;
	}

	@Override
	public void showPage(final AlbumOrder order, final int pageNumber, String sessionId) {
		DrawingArea canvas = canvases.get(pageNumber);
		PageRenderer renderer = new PageRenderer(canvas, order, pageNumber, sessionId, k);
		renderer.render();
	}

	@Override
	public void activateConvertToIndividualPageButton(boolean activate){
		if (btn == null) {
			btn = new Button("Превратить в индивидуальный", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					send(PagesMessages.CHANGE_PAGE_TYPE_TO_INDIVIDUAL);
				}
			});
		}
		if (activate) btn.enable();
		else btn.disable();
	}

	@Override
	public void selectPage(int pageNumber) {
		for (LayoutContainer c : containers) {
			c.removeStyleName("editor-spread-container-selected");
		}
		containers.get(pageNumber).addStyleName("editor-spread-container-selected");

	}

	@Override
	public void hidePages() {
		ContentPanel pagesPanel = widgets.get(EditorWidgets.PAGES_PANEL);
		pagesPanel.removeAll();
		canvases.clear();
		containers.clear();
	}

	@Override
	public void showMenu(final Order<?> order, final int currentVariantLayout) {
		ContentPanel pagesPanel = widgets.get(EditorWidgets.PAGES_PANEL);
		ToolBar toolBar = (ToolBar) pagesPanel.getTopComponent();
		toolBar.removeAll();

		Button button = new Button(constants.changePageCount(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(PagesMessages.CHANGE_PAGE_COUNT_REQUEST);
			}
		});
		toolBar.add(button);

		button = new Button(constants.clearSpread(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new ClearSpreadMessage());
			}
		});
		toolBar.add(button);

		if (order.isPackaged()) {
			if (order.getLayouts() != null && !order.getLayouts().isEmpty()) {
				layoutsField = new SelectField();
				order.sortLayouts();
				final List<Layout> _layouts = order.getLayouts();
				layoutsField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Layout>>() {
					@Override
					public void selectionChanged(SelectionChangedEvent<SelectValue<Layout>> se) {

						Layout _layout = se.getSelectedItem().getValue();
						int selectedIndex = layoutsField.getStore().indexOf(se.getSelectedItem());
						if (selectedIndex == layoutNumber) return;
						layoutNumber = selectedIndex;
						send(new SelectLayoutMessage(layoutNumber, _layout));
					}
				});
				layoutsField.setFieldLabel("Варианты: ");
				int index = 0;
				order.sortLayouts();
				for (final Iterator<Layout> iterator = order.getLayouts().iterator(); iterator.hasNext(); index++) {
					final Layout _layout = iterator.next();
					layoutsField.add(_layout, "Вариант " + (index + 1));
					if (index == currentVariantLayout) {
						layoutsField.setXValue(_layout);
					}
				}
				toolBar.add(layoutsField);
			}

			//btn.disable();
			//pagesPanel.add(btn);
			toolBar.add(btn);
		}
	}

	@Override
	public void showConvertingMessage(Integer type, final Page page, final String path, final int pageNumber, final boolean isShowMessage) {
		if (isShowMessage) {
			showNotificationWindow = new Window();
			showNotificationWindow.setHeading(constants.showNotificationWindow());
			showNotificationWindow.setLayout(new FitLayout());
			showNotificationWindow.setModal(true);
			showNotificationWindow.setSize(335, 150);
			FormPanel formPanel = new XFormPanel();
			formPanel.setHeaderVisible(false);
			final NotificationType notificationType = NotificationType.getNotificationTypeById(type);
			final Label nameField = new Label(notificationType.getMessage());
			final com.extjs.gxt.ui.client.widget.form.CheckBox cb = new com.extjs.gxt.ui.client.widget.form.CheckBox();
			formPanel.add(nameField);
			formPanel.add(cb);
			Button createButton = new Button(appConstants.ok(),
					new SelectionListener<ButtonEvent>() {
						@Override
						public void componentSelected(ButtonEvent ce) {
							if (cb.getValue()) {
								send(new CancelShowNotificationMessage(notificationType.getType()));
								send(new ChangePageTypeOnIndividualMessage(page, path, pageNumber));
							} else {
								send(new ChangePageTypeOnIndividualMessage(page, path, pageNumber));
							}
							showNotificationWindow.hide();
						}
					});
			formPanel.getButtonBar().add(cb);
			formPanel.getButtonBar().add(new Label(constants.cancelShowMessage()));
			formPanel.addButton(createButton);
			new FormButtonBinding(formPanel).addButton(createButton);
			formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					showNotificationWindow.hide();
				}
			}));
			showNotificationWindow.add(formPanel);
			showNotificationWindow.show();
		} else {
			send(new ChangePageTypeOnIndividualMessage(page, path, pageNumber));
		}
	}

	@Override
	public void hideMenu() {
		ContentPanel pagesPanel = widgets.get(EditorWidgets.PAGES_PANEL);
		ToolBar toolBar = (ToolBar) pagesPanel.getTopComponent();
		toolBar.removeAll();
		toolBar.add(new LabelToolItem(""));
	}

	@Override
	public void showPageCountWindow(Order<?> order) {
		pageCountWindow = new Window();
		pageCountWindow.setLayout(new FitLayout());
		pageCountWindow.setModal(true);
		pageCountWindow.setOnEsc(false);
		pageCountWindow.setSize(300, 150);

		FormPanel formPanel = new FormPanel();
		formPanel.setLabelWidth(100);
		formPanel.setHeaderVisible(false);

		final SelectField<Integer> pagesField = new SelectField<Integer>();
		pagesField.setFieldLabel(constants.pagesField());
		pagesField.setAllowBlank(false);
		formPanel.add(pagesField, new FormData(50, -1));

		Product product = order.getProduct();
		for (int n = product.getMinPageCount(); n <= product.getMaxPageCount(); n += product
				.getMultiplicity()) {
			pagesField.add(n, n + "");
		}
		pagesField.setXValue(order.getPageCount());

		formPanel.addButton(new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				send(new ChangePageCountMessage(pagesField.getXValue()));
			}
		}));
		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				pageCountWindow.hide();
			}
		}));

		pageCountWindow.add(formPanel);

		pageCountWindow.show();
	}

	@Override
	public void hidePageCountWindow() {
		pageCountWindow.hide();
	}

	@Override
	public void showPageCountWarning() {
		MessageBox.alert(appConstants.warning(), constants.pageCountWarning(), null);
	}
}
