package ru.imagebook.client.editor.view.order;

import java.util.List;
import java.util.Map;

import ru.imagebook.client.editor.ctl.order.CopyOrderMessage;
import ru.imagebook.client.editor.ctl.order.CreateOrderMessage;
import ru.imagebook.client.editor.ctl.order.OpenOrderMessage;
import ru.imagebook.client.editor.ctl.order.OrderView;
import ru.imagebook.shared.model.Order;
import ru.imagebook.shared.model.Product;
import ru.imagebook.shared.model.ProductType;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.gxt.form.SelectField;
import ru.minogin.core.client.gxt.form.SelectValue;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class OrderViewImpl extends View implements OrderView {
	private final OrderConstants constants;
	private final CommonConstants appConstants;

	private SelectField<Integer> typeField;
	private SelectField<Product> productField;
	private SelectField<Integer> pagesField;
	private Window createOrderWindow;
	private Window openOrderWindow;
	private SelectField<Order<?>> orderField;

	@Inject
	public OrderViewImpl(Dispatcher dispatcher, OrderConstants constants, CommonConstants appConstants) {
		super(dispatcher);

		this.constants = constants;
		this.appConstants = appConstants;
	}

	@Override
	public void showOrderForm(final Map<Integer, List<Product>> products, final String locale,
			List<Order<?>> orders) {
		createOrderWindow = new Window();
		createOrderWindow.setHeading(constants.orderFormWindowHeading());
		createOrderWindow.setLayout(new FitLayout());
		createOrderWindow.setSize(800, 200);
		createOrderWindow.setModal(true);
		createOrderWindow.setOnEsc(false);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(150);

		final RadioGroup actionField = new RadioGroup();
		actionField.setHideLabel(true);

		final Radio newActionField = new Radio();
		newActionField.setBoxLabel(constants.newActionField());
		actionField.add(newActionField);

		final Radio copyActionField = new Radio();
		copyActionField.setBoxLabel(constants.copyActionField());
		actionField.add(copyActionField);

		formPanel.add(actionField);

		typeField = new SelectField<Integer>();
		typeField.setFieldLabel(constants.type());
		typeField.setAllowBlank(false);
		for (int type : products.keySet()) {
			typeField.add(type, ProductType.values.get(type).get(locale));
		}
		typeField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Integer>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Integer>> se) {
				productField.clear();
				productField.getStore().removeAll();

				Integer type = typeField.getXValue();
				List<Product> typeProducts = products.get(type);
				if (typeProducts != null) {
					for (Product product : typeProducts) {
						productField.add(product, product.getName().get(locale));
					}
				}
			}
		});
		formPanel.add(typeField, new FormData(500, -1));

		productField = new SelectField<Product>();
		productField.setFieldLabel(constants.product());
		productField.setAllowBlank(false);
		productField.addSelectionChangedListener(new SelectionChangedListener<SelectValue<Product>>() {
			@Override
			public void selectionChanged(SelectionChangedEvent<SelectValue<Product>> se) {
				pagesField.clear();
				pagesField.getStore().removeAll();

				SelectValue<Product> selectedItem = se.getSelectedItem();
				if (selectedItem != null) {
					Product product = selectedItem.getValue();
					for (int n = product.getMinPageCount(); n <= product.getMaxPageCount(); n += product
							.getMultiplicity()) {
						pagesField.add(n, n + "");
					}
					pagesField.setXValue(product.getMinPageCount());
				}
			}
		});
		formPanel.add(productField, new FormData(500, -1));

		pagesField = new SelectField<Integer>();
		pagesField.setFieldLabel(constants.pagesField());
		pagesField.setAllowBlank(false);
		formPanel.add(pagesField, new FormData(50, -1));

		orderField = new SelectField<Order<?>>();
		orderField.setAllowBlank(false);
		orderField.setVisible(false);
		orderField.setFieldLabel(constants.orderField());
		for (Order<?> order : orders) {
			orderField.add(order, order.getNumber() + " - " + order.getProduct().getName().get(locale));
		}
		formPanel.add(orderField, new FormData(500, -1));

		Button okButton = new Button(constants.okButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Radio radio = actionField.getValue();
				if (radio == newActionField)
					send(new CreateOrderMessage(productField.getXValue().getId(), pagesField.getXValue()));
				else if (radio == copyActionField)
					send(new CopyOrderMessage(orderField.getXValue().getId()));
			}
		});
		formPanel.addButton(okButton);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(okButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				createOrderWindow.hide();
			}
		}));

		actionField.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent event) {
				Radio radio = actionField.getValue();
				if (radio == newActionField) {
					typeField.show();
					typeField.enable();

					productField.show();
					productField.enable();

					pagesField.show();
					pagesField.enable();

					orderField.hide();
					orderField.disable();
				}
				else if (radio == copyActionField) {
					typeField.hide();
					typeField.disable();

					productField.hide();
					productField.disable();

					pagesField.hide();
					pagesField.disable();

					orderField.show();
					orderField.enable();
				}
			}
		});

		actionField.setValue(newActionField);

		createOrderWindow.add(formPanel);

		createOrderWindow.show();
	}

	@Override
	public void hideCreateOrderForm() {
		createOrderWindow.hide();
	}

	@Override
	public void showOpenOrderForm(List<Order<?>> orders, String locale) {
		openOrderWindow = new Window();
		openOrderWindow.setHeading(constants.openOrderWindowHeading());
		openOrderWindow.setLayout(new FitLayout());
		openOrderWindow.setSize(750, 150);
		openOrderWindow.setModal(true);
		openOrderWindow.setOnEsc(false);

		FormPanel formPanel = new FormPanel();
		formPanel.setHeaderVisible(false);
		formPanel.setLabelWidth(150);

		final SelectField<Order<?>> orderField = new SelectField<Order<?>>();
		orderField.setAllowBlank(false);
		orderField.setFieldLabel(constants.orderField());
		for (Order<?> order : orders) {
			orderField.add(order, order.getNumber() + " - " + order.getProduct().getName().get(locale));
		}
		formPanel.add(orderField, new FormData(550, -1));

		Button openButton = new Button(constants.openButton(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				Order<?> order = orderField.getXValue();
				send(new OpenOrderMessage(order.getId()));
			}
		});
		formPanel.addButton(openButton);

		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(openButton);

		formPanel.addButton(new Button(appConstants.cancel(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				openOrderWindow.hide();
			}
		}));

		openOrderWindow.add(formPanel);

		openOrderWindow.show();
	}

	@Override
	public void hideOpenOrderForm() {
        if (openOrderWindow != null) {
            openOrderWindow.hide();
        }
	}

	@Override
	public void showEditorCommonMessage(final String errorMessage) {
		new ru.minogin.core.client.gwt.ui.MessageBox(appConstants.error(), errorMessage, appConstants).show();
	}

	@Override
	public void infoOrderProcessed() {
		MessageBox.info(appConstants.info(), constants.orderProcessed(), null);
	}
}
