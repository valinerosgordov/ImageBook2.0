package ru.imagebook.client.calc.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.calc.ctl.CalcView;
import ru.imagebook.client.calc.ctl.ColorSelectedMessage;
import ru.imagebook.client.calc.ctl.CoverLamSelectedMessage;
import ru.imagebook.client.calc.ctl.LevelSelectedMessage;
import ru.imagebook.client.calc.ctl.PageLamSelectedMessage;
import ru.imagebook.client.calc.ctl.PagesSelectedMessage;
import ru.imagebook.client.calc.ctl.ProductSelectedMessage;
import ru.imagebook.client.calc.ctl.QuantitySelectedMessage;
import ru.imagebook.client.calc.ctl.TypeSelectedMessage;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.CoverLamination;
import ru.imagebook.shared.model.PageLamination;
import ru.imagebook.shared.model.Paper;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CalcViewImpl extends View implements CalcView {
	private final CalcConstants constants;
	private VerticalPanel panel;
	private String locale;
	private VerticalPanel productValuesPanel;
	private VerticalPanel productPanel;
	private ListBox pagesBox;
	private VerticalPanel colorValuesPanel;
	private VerticalPanel otherPanel;
	private HTML priceHtml;
	private VerticalPanel coverLamValuesPanel;
	private VerticalPanel pageLamValuesPanel;
	private HTML costHtml;
	private HTML discountHtml;
	private HTML totalHtml;
	private TextBox quantityField;
	private FlexTable priceTable;
	private HorizontalPanel colorPanel;
	private HorizontalPanel coverLamPanel;
	private HorizontalPanel pageLamPanel;
	private HTML quantityHtml;
	private HTML productHtml;
	private ListBox levelBox;
	private HTML infoHtml;
	private VerticalPanel pricePanel;
	private HTML formatHtml;
	private HTML paperHtml;
	private Map<Integer, RadioButton> typeRadios;
	private Map<Product, RadioButton> productRadios;

	@Inject
	public CalcViewImpl(Dispatcher dispatcher, CalcConstants constants) {
		super(dispatcher);

		this.constants = constants;
	}

	@Override
	public void showCalc(Integer productId) {
		locale = constants.locale();

		panel = new VerticalPanel();

		if (productId != null)
			RootPanel.get("webcalc").add(panel);
		else {
			panel.addStyleName("calc-panel");
			panel.setSize("100%", "100%");
			RootPanel.get().add(panel);
		}
	}

	@Override
	public void showForm(List<Integer> types, Integer productId) {
		if (productId == null) {
			HorizontalPanel row1Panel = new HorizontalPanel();
			row1Panel.addStyleName("row1-panel");

			VerticalPanel typePanel = new VerticalPanel();
			typePanel.setWidth("400px");
			HTML typeTitle = new HTML(constants.typeField() + ":");
			typeTitle.addStyleName("field-title");
			typePanel.add(typeTitle);
			typeRadios = new HashMap<Integer, RadioButton>();
			for (final int type : types) {
				String text = ProductType.values.get(type).get(locale);
				RadioButton radio = new RadioButton("type", text);
				radio.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						send(new TypeSelectedMessage(type));
					}
				});
				typePanel.add(radio);
				typeRadios.put(type, radio);
			}

			row1Panel.add(typePanel);

			productPanel = new VerticalPanel();
			productPanel.addStyleName("product-panel");
			productPanel.setVisible(false);

			HTML productTitle = new HTML(constants.productField() + ":");
			productTitle.addStyleName("field-title");
			productPanel.add(productTitle);

			productValuesPanel = new VerticalPanel();
			productPanel.add(productValuesPanel);

			row1Panel.add(productPanel);

			panel.add(row1Panel);
		}

		HorizontalPanel row2Panel = new HorizontalPanel();

		otherPanel = new VerticalPanel();
		if (productId == null)
			otherPanel.addStyleName("other-panel");
		otherPanel.setWidth("300px");
		otherPanel.setVisible(false);

		HorizontalPanel pagesPanel = new HorizontalPanel();
		HTML pagesTitle = new HTML(constants.pagesField() + ":");
		pagesTitle.addStyleName("field-title");
		pagesTitle.addStyleName("other-title");
		pagesPanel.add(pagesTitle);
		pagesBox = new ListBox();
		pagesPanel.add(pagesBox);
		otherPanel.add(pagesPanel);

		colorPanel = new HorizontalPanel();
		colorPanel.addStyleName("color-panel");
		colorPanel.setVisible(false);
		HTML colorTitle = new HTML(constants.colorField() + ":");
		colorTitle.addStyleName("field-title");
		colorTitle.addStyleName("other-title");
		colorPanel.add(colorTitle);
		colorValuesPanel = new VerticalPanel();
		colorPanel.add(colorValuesPanel);
		otherPanel.add(colorPanel);

		coverLamPanel = new HorizontalPanel();
		coverLamPanel.addStyleName("cover-lam-panel");
		coverLamPanel.setVisible(false);
		HTML coverLamTitle = new HTML(constants.coverLamField() + ":");
		coverLamTitle.addStyleName("field-title");
		coverLamTitle.addStyleName("other-title");
		coverLamPanel.add(coverLamTitle);
		coverLamValuesPanel = new VerticalPanel();
		coverLamPanel.add(coverLamValuesPanel);
		otherPanel.add(coverLamPanel);

		pageLamPanel = new HorizontalPanel();
		pageLamPanel.addStyleName("page-lam-panel");
		pageLamPanel.setVisible(false);
		HTML pageLamTitle = new HTML(constants.pageLamField() + ":");
		pageLamTitle.addStyleName("field-title");
		pageLamTitle.addStyleName("other-title");
		pageLamPanel.add(pageLamTitle);
		pageLamValuesPanel = new VerticalPanel();
		pageLamPanel.add(pageLamValuesPanel);
		otherPanel.add(pageLamPanel);

		HorizontalPanel quantityPanel = new HorizontalPanel();
		quantityPanel.addStyleName("quantity-panel");
		HTML quantityTitle = new HTML(constants.quantity() + ": *");
		quantityTitle.addStyleName("field-title");
		quantityTitle.addStyleName("other-title");
		quantityPanel.add(quantityTitle);
		quantityField = new TextBox();
		quantityField.addStyleName("quantity-field");
		quantityField.setWidth("30px");
		quantityField.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				onQuantityChange();
			}
		});
		quantityField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				onQuantityChange();
			}
		});
		quantityPanel.add(quantityField);
		otherPanel.add(quantityPanel);

		HorizontalPanel levelPanel = new HorizontalPanel();
		levelPanel.addStyleName("level-panel");
		HTML levelTitle = new HTML(constants.levelField() + ":");
		levelTitle.addStyleName("field-title");
		levelTitle.addStyleName("other-title");
		levelPanel.add(levelTitle);
		levelBox = new ListBox();
		for (int i = 0; i <= 8; i++) {
			String value;
			if (i > 0)
				value = i + "";
			else
				value = constants.noLevel();

			levelBox.addItem(value, i + "");
		}
		levelBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String value = levelBox.getValue(levelBox.getSelectedIndex());
				send(new LevelSelectedMessage(new Integer(value)));
			}
		});
		levelPanel.add(levelBox);
		otherPanel.add(levelPanel);

		row2Panel.add(otherPanel);

		pricePanel = new VerticalPanel();
		if (productId == null)
			pricePanel.addStyleName("price-panel");
		else
			pricePanel.addStyleName("price-panel-web");

		if (productId == null) {
			productHtml = new HTML();
			productHtml.addStyleName("product-html");
			pricePanel.add(productHtml);

			formatHtml = new HTML();
			formatHtml.addStyleName("format-html");
			pricePanel.add(formatHtml);

			paperHtml = new HTML();
			paperHtml.addStyleName("format-html");
			pricePanel.add(paperHtml);
		}

		priceTable = new FlexTable();
		priceTable.setVisible(false);
		if (productId == null)
			priceTable.addStyleName("price-table");
		priceTable.getCellFormatter().setWidth(0, 0, "300px");
		int y = 0;

		priceTable.setHTML(y, 0, constants.price() + ":");
		priceHtml = new HTML();
		priceTable.setWidget(y, 1, priceHtml);
		priceTable.getCellFormatter().setHorizontalAlignment(y, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		y++;

		priceTable.setHTML(y, 0, constants.quantity() + ":");
		quantityHtml = new HTML();
		priceTable.setWidget(y, 1, quantityHtml);
		priceTable.getCellFormatter().setHorizontalAlignment(y, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		y++;

		priceTable.setHTML(y, 0, constants.cost() + ":");
		costHtml = new HTML();
		priceTable.setWidget(y, 1, costHtml);
		priceTable.getCellFormatter().setHorizontalAlignment(y, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		y++;

		priceTable.setHTML(y, 0, constants.discount() + ":");
		discountHtml = new HTML();
		priceTable.setWidget(y, 1, discountHtml);
		priceTable.getCellFormatter().setHorizontalAlignment(y, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		y++;

		HTML totalTitle = new HTML(constants.total() + ":");
		totalTitle.addStyleName("total-title");
		priceTable.setWidget(y, 0, totalTitle);
		totalHtml = new HTML();
		totalHtml.addStyleName("total-html");
		priceTable.setWidget(y, 1, totalHtml);
		priceTable.getCellFormatter().setHorizontalAlignment(y, 1,
				HasHorizontalAlignment.ALIGN_RIGHT);
		y++;

		pricePanel.add(priceTable);

		row2Panel.add(pricePanel);

		panel.add(row2Panel);

		infoHtml = new HTML(constants.info());
		infoHtml.addStyleName("info-html");
		infoHtml.setVisible(false);
		panel.add(infoHtml);
	}

	private void onQuantityChange() {
		String value = quantityField.getValue();
		Integer quantity = 1;
		try {
			quantity = new Integer(value);
		}
		catch (Exception e) {
		}
		if (quantity <= 0)
			quantity = 1;
		if (quantity > 300)
			quantity = 300;
		send(new QuantitySelectedMessage(quantity));
	}

	@Override
	public void showProductField(List<Product> typeProducts) {
		productValuesPanel.clear();

		productRadios = new HashMap<Product, RadioButton>();
		for (final Product product : typeProducts) {
			String text = product.getName().get(locale);
			RadioButton radio = new RadioButton("product", text);
			radio.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					send(new ProductSelectedMessage(product));
				}
			});
			productValuesPanel.add(radio);
			productRadios.put(product, radio);
		}
		productPanel.setVisible(true);
		otherPanel.setVisible(false);
		pricePanel.setVisible(false);
		infoHtml.setVisible(false);
	}

	@Override
	public void showOtherFields(Product product, Map<Integer, Color> colors,
			Color colorValue, Integer coverLamValue, Integer pageLamValue,
			Integer quantity) {
		pagesBox.clear();
		for (int pages = product.getMinPageCount(); pages <= product
				.getMaxPageCount(); pages += product.getMultiplicity()) {
			pagesBox.addItem(pages + "");
		}
		pagesBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String value = pagesBox.getValue(pagesBox.getSelectedIndex());
				send(new PagesSelectedMessage(new Integer(value)));
			}
		});

		colorValuesPanel.clear();
		if (product.getColorRange().size() > 1) {
			for (int colorNumber : product.getColorRange()) {
				final Color color = colors.get(colorNumber);
				String text = color.getName().get(locale);
				RadioButton radio = new RadioButton("color", text);
				if (color == colorValue)
					radio.setValue(true);
				radio.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						send(new ColorSelectedMessage(color));
					}
				});
				colorValuesPanel.add(radio);
			}
			colorPanel.setVisible(true);
		}
		else
			colorPanel.setVisible(false);

		coverLamValuesPanel.clear();
		if (product.getCoverLamRange().size() > 1) {
			for (final int coverLam : product.getCoverLamRange()) {
				String text = CoverLamination.values.get(coverLam).get(locale);
				RadioButton radio = new RadioButton("coverLam", text);
				if (coverLam == coverLamValue)
					radio.setValue(true);
				radio.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						send(new CoverLamSelectedMessage(coverLam));
					}
				});
				coverLamValuesPanel.add(radio);
			}
			coverLamPanel.setVisible(true);
		}
		else
			coverLamPanel.setVisible(false);

		pageLamValuesPanel.clear();
		if (product.getPageLamRange().size() > 1) {
			for (final int pageLam : product.getPageLamRange()) {
				String text = PageLamination.values.get(pageLam).get(locale);
				RadioButton radio = new RadioButton("pageLam", text);
				if (pageLam == pageLamValue)
					radio.setValue(true);
				radio.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						send(new PageLamSelectedMessage(pageLam));
					}
				});
				pageLamValuesPanel.add(radio);
			}
			pageLamPanel.setVisible(true);
		}
		else
			pageLamPanel.setVisible(false);

		quantityField.setValue(quantity + "");

		otherPanel.setVisible(true);
		pricePanel.setVisible(true);
		infoHtml.setVisible(true);
	}

	@Override
	public void showPrice(Product product, int price, int quantity, int cost,
			int discount, int total, Integer productId) {
		if (productId == null) {
			productHtml.setHTML(product.getName().get(locale));
			formatHtml.setHTML(constants.format() + ": " + product.getBlockFormat());
			String paperText = Paper.values.get(product.getPaper()).get(locale);
			paperHtml.setHTML(constants.paper() + ": " + paperText);
		}
		priceHtml.setHTML(price + " руб.");
		quantityHtml.setHTML(quantity + "");
		costHtml.setHTML(cost + " руб.");
		discountHtml.setHTML(discount + " руб.");
		totalHtml.setHTML(total + " руб.");
		priceTable.setVisible(true);
	}

	@Override
	public void hidePrice() {
		priceTable.setVisible(false);
	}

	@Override
	public void selectProduct(Product product) {
		RadioButton typeRadio = typeRadios.get(product.getType());
		typeRadio.setValue(true);

		RadioButton productRadio = productRadios.get(product);
		productRadio.setValue(true);
	}
}
