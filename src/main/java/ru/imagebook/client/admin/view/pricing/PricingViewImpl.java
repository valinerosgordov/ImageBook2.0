package ru.imagebook.client.admin.view.pricing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.imagebook.client.admin.ctl.pricing.LeavingConfirmedMessage;
import ru.imagebook.client.admin.ctl.pricing.PricingView;
import ru.imagebook.client.admin.ctl.pricing.SavePricingDataMessage;
import ru.imagebook.client.admin.view.DesktopWidgets;
import ru.imagebook.shared.model.Color;
import ru.imagebook.shared.model.pricing.PricingData;
import ru.minogin.core.client.constants.CommonConstants;
import ru.minogin.core.client.flow.Dispatcher;
import ru.minogin.core.client.flow.Message;
import ru.minogin.core.client.flow.View;
import ru.minogin.core.client.flow.Widgets;
import ru.minogin.core.client.gxt.ConfirmMessageBox;
import ru.minogin.core.client.gxt.form.DoubleField;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FormButtonBinding;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.inject.Inject;

public class PricingViewImpl extends View implements PricingView {
	private final CommonConstants appConstants;
	private final Map<String, NumberField> fields = new HashMap<String, NumberField>();

	private FormPanel formPanel;
	private PricingData data;
	private final Widgets widgets;
	private final PricingConstants constants;

	@Inject
	public PricingViewImpl(Dispatcher dispatcher, Widgets widgets, CommonConstants appConstants,
			PricingConstants constants) {
		super(dispatcher);

		this.widgets = widgets;
		this.appConstants = appConstants;
		this.constants = constants;
	}

	@Override
	public void show(final PricingData data, List<Color> colors, String locale) {
		this.data = data;

		LayoutContainer desktop = widgets.get(DesktopWidgets.DESKTOP);
		desktop.removeAll();

		formPanel = new FormPanel();
		formPanel.setHeading("Данные для расчета цен");
		formPanel.setLabelWidth(300);
		formPanel.setScrollMode(Scroll.AUTO);

		addLabel("1. Стоимость печати условного листа (Ца3)");
		addField("Ца3_1", "Everflat А4");
		addField("Ца3_2", "Everflat 33х33");
		addField("Ца3_3", "Меловка А3");

		addLabel("2. Коэффициент удорожания печати при ламинации страниц (Кламстр)");
		addField("Кламстр_1", "Глянцевая ламинация");
		addField("Кламстр_2", "Матовая ламинация");
		addField("Кламстр_3", "Песок");

		addLabel("3. Стоимость фальцовки листа А3 (Цфальц)");
		addField("Цфальц_1", "Фальцовка листа А3");

		addLabel("4. Стоимость глянцевой ламинации обложки для Миниальбома (Цламоблм)");
		addField("Цламоблм_1", "Ламинация А4 (А5+А5)");

		addLabel("5. Коэффициент удорожания стоимости ламинации обложки альбома на скрепке относительно формата миниальбома (Кформмп)");
		addField("Кформмп_1", "Буклет (21х21)");
		addField("Кформмп_2", "Книга (21х25)");
		addField("Кформмп_3", "Альбом (А4 —  21х30)");

		addLabel("6. Коэффициент удорожания для матовой ламинации обложки альбомов в мягком переплете (Кламобмп)");
		addField("Кламобмп_1", "Матовая ламинация");

		addLabel("7. Коэффициент удорожания стоимости обложки альбома на пружине относительно формата миниальбома (Кформпру)");
		addField("Кформпру_1", "Буклет (21х21)");
		addField("Кформпру_2", "Книга (21х25)");
		addField("Кформпру_3", "Альбом (А4 —  21х30)");
		addField("Кформпру_4", "Элит (30х30)");
		addField("Кформпру_5", "Суперальбом (42х30)");

		addLabel("8. Стоимость пружинного переплета с бесцветным пластиком для Миниальбома (Цпру)");
		addField("Цпру_1", "Миниальбом");

		addLabel("9. Коэффициент удорожания стоимости обложки альбома на пружине относительно бесцветного пластика (Кцоблпру)");
		for (Color color : colors) {
			if (color.getNumber() == 0)
				continue;

			String name = "Кцоблпру_" + color.getNumber();
			addField(name, color.getName().getNonEmptyValue(locale));
		}

		addLabel("10. Коэффициент удорожания для матовой ламинации белой и плоттерной обложек (Кламоб)");
		addField("Кламоб_1", "Матовая ламинация");

		addLabel("11. Коэффициент удорожания стоимости стандартной обложки относительно формата миниальбома (Кформстоб) ");
		addField("Кформстоб_1", "Буклет (21х21)");
		addField("Кформстоб_2", "Книга (21х25)");
		addField("Кформстоб_3", "Альбом (А4 —  21х30)");
		addField("Кформстоб_4", "Элит (30х30)");
		addField("Кформстоб_5", "Суперальбом (42х30)");

		addLabel("12. Стоимость стандартной обложки для Миниальбома (Цстоб)");
		addField("Цстоб_1", "Миниальбом");

		addLabel("13. Коэффициент удорожания стоимости плоттерной обложки относительно формата миниальбома (Кформплотт) ");
		addField("Кформплотт_1", "Буклет (21х21)");
		addField("Кформплотт_2", "Книга (21х25)");
		addField("Кформплотт_3", "Альбом (А4 —  21х30)");
		addField("Кформплотт_4", "Элит (30х30)");
		addField("Кформплотт_5", "Суперальбом (42х30)");

		addLabel("14. Стоимость плоттерной обложки для Миниальбома (Цстоб)");
		addField("Цплотт_1", "Миниальбом");

		addLabel("15. Коэффициент удорожания стоимости кожаной обложки относительно формата миниальбома (Кформ) ");
		addField("Кформ_1", "Буклет (21х21)");
		addField("Кформ_2", "Книга (21х25)");
		addField("Кформ_3", "Альбом (А4 —  21х30)");
		addField("Кформ_4", "Элит (30х30)");
		addField("Кформ_5", "Суперальбом (42х30)");

		addLabel("16. Стоимость кожаной обложки для Миниальбома (Цкож)");
		addField("Цкож_1", "Миниальбом");

		addLabel("17. Коэффициент удорожания стоимости обложки альбома в коже относительно стандартной кожи (Кцоблкож)");
		for (Color color : colors) {
			if (color.getNumber() == 0)
				continue;

			String name = "Кцоблкож_" + color.getNumber();
			addField(name, color.getName().getNonEmptyValue(locale));
		}

		addLabel("18. Стоимостные коэффициенты ИМИДЖБУКА");
		addField("КИ11", "Печать на Everflat А4");
		addField("КИ12", "Печать на Everflat Элит");
		addField("КИ21", "Плоттер");
		addField("КИ22", "Кожа");
		addField("КИ3", "Ламинация страниц и обложки");
		addField("КИ4", "Меловка, пружинный и обычный переплеты");

		addLabel("19. Таблица скидок при увеличении количества экземпляров");
		addField("IB_1", "1 экз");
		addField("IB_2", "2 экз");
		addField("IB_3", "3-5 экз");
		addField("IB_4", "6-10 экз");
		addField("IB_5", "11-20 экз");
		addField("IB_6", "21-40 экз");
		addField("IB_7", "41-60 экз");
		addField("IB_8", "61-100 экз");
		addField("IB_9", "101-300 экз");

		addLabel("20. Таблица скидок - панорамные альбомы (BB = 01, 02)");
		addField("IB_EVERFLAT_1", "1 экз");
		addField("IB_EVERFLAT_2", "2 экз");
		addField("IB_EVERFLAT_3", "3-5 экз");
		addField("IB_EVERFLAT_4", "6-10 экз");
		addField("IB_EVERFLAT_5", "11-20 экз");
		addField("IB_EVERFLAT_6", "21-40 экз");
		addField("IB_EVERFLAT_7", "41-60 экз");
		addField("IB_EVERFLAT_8", "61-100 экз");
		addField("IB_EVERFLAT_9", "101-300 экз");

		addLabel("21. Пробный альбом");
		addField("TAPPH", "Стоимость пробного альбома для типографии");
		addField("TAPC", "Стоимость пробного альбома для клиента");

		addLabel("22. Поздравительный альбом");
		addField("ПоздравительныйСебестоимость", "Стоимость поздравительного альбома для типографии");
		addField("ПоздравительныйСтоимость", "Стоимость поздравительного альбома для клиента");

		Button saveButton = new Button(appConstants.save(), new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				for (String key : fields.keySet()) {
					NumberField field = fields.get(key);
					data.set(key, field.getValue());
				}

				SavePricingDataMessage message = new SavePricingDataMessage(data);
				send(message);
			}
		});
		formPanel.addButton(saveButton);
		FormButtonBinding binding = new FormButtonBinding(formPanel);
		binding.addButton(saveButton);

		formPanel.setButtonAlign(HorizontalAlignment.LEFT);

		desktop.add(formPanel);
		desktop.layout();
	}

	private void addLabel(String text) {
		LabelField label = new LabelField(text);
		label.setHideLabel(true);
		formPanel.add(label, new FormData(600, -1));
	}

	private void addField(String name, String label) {
		DoubleField field = new DoubleField();
		field.setPropertyEditorType(Double.class);
		field.setFieldLabel(label + " (" + name + ")");
		field.setValue((Double) data.get(name));
		field.setAllowBlank(false);
		formPanel.add(field);

		fields.put(name, field);
	}

	@Override
	public void confirmLeaving(final Message message) {
		new ConfirmMessageBox(appConstants.warning(), constants.confirmLeaving(),
				new Listener<BaseEvent>() {
					@Override
					public void handleEvent(BaseEvent be) {
						send(new LeavingConfirmedMessage(message));
					}
				});
	}
}
