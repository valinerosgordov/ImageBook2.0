package ru.imagebook.client.app.view.payment;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.app.ctl.payment.DeliveryPresenter;
import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.shared.model.DeliveryType;
import ru.imagebook.shared.model.app.SDEKCourierData;
import ru.imagebook.shared.model.app.SDEKPackage;
import ru.imagebook.shared.model.app.SDEKPickupData;

public class SDEKWidget implements IsWidget {
	interface SDEKWidgetUiBinder extends UiBinder<Widget, SDEKWidget> {
	}
	private static SDEKWidgetUiBinder uiBinder = GWT.create(SDEKWidgetUiBinder.class);

	@UiField
	HTMLPanel sdekWidgetPanel;
	@UiField
	Label sdekWidgetDisabledLabel;
	@UiField
	VerticalPanel costAndTimeFieldPanel;
	@UiField
	SpanElement costField;
	@UiField
	SpanElement timeField;
	@UiField
	HorizontalPanel courierCompanyPanel;
	@UiField
	HorizontalPanel pickupCompanyPanel;
	@UiField
	SpanElement pickUpCompanyField;

	private DeliveryPresenter presenter;

	@Override
	public Widget asWidget() {
		return uiBinder.createAndBindUi(this);
	}

	public void setPresenter(DeliveryPresenter presenter) {
		this.presenter = presenter;
	}

	public void show(List<SDEKPackage> sdekPackages) {
		sdekWidgetPanel.setVisible(true);

		String servicePath = GWT.getHostPageBaseURL() + "sdek/service.php";
		showSDEKWidget("sdekWidget", servicePath);

		for (SDEKPackage p : sdekPackages) {
			//TODO remove
			GWT.log("added package [l=" + p.getLength() + ", w=" + p.getWidth() + ", h=" + p.getHeight() + ", wt=" + p.getWeight());
			addGoodToWidget(p.getLength(), p.getWidth(), p.getHeight(), p.getWeight());
		}
		// TODO widget display error sdekWidgetDisabledLabel.setVisible(true)
	}

	public void hide() {
		sdekWidgetPanel.setVisible(false);
		sdekWidgetDisabledLabel.setVisible(false);
		costAndTimeFieldPanel.setVisible(false);
	}

	private void onPickupSelected(SDEKPickupData pickupData) {
		courierCompanyPanel.setVisible(false);
		pickupCompanyPanel.setVisible(true);
		pickUpCompanyField.setInnerText("Пункт самовывоза: \"" + pickupData.getName() + "\", "
			+ "адрес: " + pickupData.getCityName() + ", " + pickupData.getAddress());
		timeField.setInnerText(getTimeString(pickupData.getTerm()));
		setCostFieldText(costField, pickupData.getPrice());
		costAndTimeFieldPanel.setVisible(true);
		scrollToSelectedDeliveryMethodInfo();
		presenter.onSDEKPickupSelected(pickupData);
	}

	private void onCourierSelected(SDEKCourierData courierData) {
		pickupCompanyPanel.setVisible(false);
		courierCompanyPanel.setVisible(true);
		timeField.setInnerText(getTimeString(courierData.getTerm()));
		setCostFieldText(costField, courierData.getPrice());
		costAndTimeFieldPanel.setVisible(true);
		scrollToSelectedDeliveryMethodInfo();
		presenter.onSDEKCourierSelected(courierData);
	}

	private String getTimeString(String term) {
		String[] terms = term.split("-");
		String ret = "от " + terms[0];
		if (terms.length > 1) {
			ret += " до " + terms[1];
		}
		return ret + " дн.";
	}

	private void setCostFieldText(SpanElement costField, int cost) {
		int discountPc = presenter.computeDeliveryDiscountPc(DeliveryType.SDEK);
		if (discountPc > 0) {
			int costByDiscount = CostCalculatorImpl.computeCostByDiscount(cost, discountPc);
			String costFieldText = "<strike>" + cost + " руб.</strike> <b><font color='red'>"
				+ costByDiscount + " руб.";
			costField.setInnerHTML(costFieldText);
		} else {
			costField.setInnerText(cost + " руб.");
		}
	}

	private native void showSDEKWidget(String elementId, String servicePath) /*-{
    	var that = this;
        $wnd.widjet = new $wnd.ISDEKWidjet ({
            cityFrom: 'Москва', // из какого города будет идти доставка
            country: 'Россия', // можно выбрать страну, для которой отображать список ПВЗ
            link: elementId, // id элемента страницы, в который будет вписан виджет
            path: 'https://widget.cdek.ru/widget/scripts/', //директория с бибилиотеками
            servicepath: servicePath, //ссылка на файл service.php на вашем сайте
			apikey: 'a59de155-dcb6-4edb-9f12-b9ab4dc30803',
			onChoose: function onChoose(wat) {
				console.log(wat);
				that.@ru.imagebook.client.app.view.payment.SDEKWidget::onPickupSelected(Lru/imagebook/shared/model/app/SDEKPickupData;)(wat);
			},
			onChooseProfile: function onChooseProfile(wat) {
                console.log(wat);
				that.@ru.imagebook.client.app.view.payment.SDEKWidget::onCourierSelected(Lru/imagebook/shared/model/app/SDEKCourierData;)(wat);
			}
        });
    }-*/;

    private native void addGoodToWidget(int length, int width, int height, double weight) /*-{
		$wnd.widjet.cargo.add({
			length: length,
			width: width,
			height: height,
			weight: weight
		});
	}-*/;

	private native void scrollToSelectedDeliveryMethodInfo() /*-{
		$wnd.$('.sdek-cost-and-time').ScrollTo();
	}-*/;
}
