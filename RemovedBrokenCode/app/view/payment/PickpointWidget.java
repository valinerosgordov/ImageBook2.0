package ru.imagebook.client.app.view.payment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import ru.imagebook.client.common.service.CostCalculatorImpl;
import ru.imagebook.shared.model.Bill;
import ru.imagebook.shared.model.app.PickPointData;

public class PickpointWidget extends Form implements IsWidget {
	interface PickpointWidgetUiBinder extends UiBinder<Widget, PickpointWidget> {
	}

	@UiField
	TextBox pickpointPostamateId;
//	@UiField
//	TextBox pickpointRateZone;
//	@UiField
//	TextBox pickpointCoeff;
	@UiField
	TextBox pickpointAddressField;
	@UiField
	VerticalPanel pickpointCostAndTimeFieldPanel;
	@UiField
	HTMLPanel pickpointWidgetPanel;
	@UiField
	SpanElement pickpointCostField;
	@UiField
	SpanElement pickpointTimeField;

	private static PickpointWidgetUiBinder uiBinder = GWT.create(PickpointWidgetUiBinder.class);
	private static DeliveryConstants constants = GWT.create(DeliveryConstants.class);

	private final HandlerManager handlerManager = new HandlerManager(this);

	@Override
	public Widget asWidget() {
		Widget ui = uiBinder.createAndBindUi(this);
		addFields();
		return ui;
	}

	public void addFields() {
		allFields.clear();
		allFields.add(pickpointPostamateId);
		allFields.add(pickpointAddressField);
		//allFields.add(pickpointRateZone);
		//allFields.add(pickpointCoeff);

		mandatoryFields.clear();
		mandatoryFields.add(pickpointPostamateId);
		mandatoryFields.add(pickpointAddressField);
		// Because widget returns null
		//mandatoryFields.add(pickpointRateZone);
		//mandatoryFields.add(pickpointCoeff);
	}

	public void fetch(Bill bill) {
		bill.setPickpointPostamateID(getValue(pickpointPostamateId));
		bill.setPickpointAddress(getValue(pickpointAddressField));
		//bill.setPickpointRateZone(getValue(pickpointRateZone));
		//bill.setPickpointTrunkCoeff(getDoubleValue(pickpointCoeff));
	}

	public void showWidget() {
		toggleCostAndTimeFieldPanel();
		pickpointWidgetPanel.setVisible(true);
		firePostamateSelectedEvent();
	}

	public void showCostAndTime(PickPointData data, int discountPc) {
		toggleCostAndTimeFieldPanel();
		int cost = data.getCost();

		if (discountPc > 0) {
			int costByDiscount = CostCalculatorImpl.computeCostByDiscount(cost, discountPc);
			pickpointCostField.setInnerHTML(constants.pickpointDiscountedCostField(cost, costByDiscount));
		} else {
			pickpointCostField.setInnerText(constants.pickpointCostField(data.getCost()));
		}

		String timeMsg;
		if (data.getTimeMin() != data.getTimeMax() && data.getTimeMax() > 0) {
			timeMsg = constants.pickpointTimeFieldRange(data.getTimeMin(), data.getTimeMax());
		} else {
			timeMsg = constants.pickpointTimeFieldSingleValue(data.getTimeMin());
		}
		pickpointTimeField.setInnerText(timeMsg);
	}

	public void hideCostAndTime() {
		pickpointCostAndTimeFieldPanel.setVisible(false);
	}

	private void toggleCostAndTimeFieldPanel() {
		pickpointCostAndTimeFieldPanel.setVisible(!isEmpty(pickpointAddressField));
	}

	public void hideWidget() {
		pickpointWidgetPanel.setVisible(false);
		pickpointCostAndTimeFieldPanel.setVisible(false);
	}

	@UiHandler("pickpointAddressField")
	void onAddressClick(ClickEvent event) {
		showPickpointForm();
	}

	@UiHandler("pickpointAddressField")
	void onAddressChange(ChangeEvent event) {
        if (isComplete()) {
            handlerManager.fireEvent(new PostamateSelectionEvent());
        }
	}

    public HandlerRegistration addPostamateSelectionEventHandler(PostamateSelectionEventHandler handler) {
        return handlerManager.addHandler(PostamateSelectionEvent.TYPE, handler);
    }

    private void firePostamateSelectedEvent() {
		if (isComplete()) {
            handlerManager.fireEvent(new PostamateSelectionEvent());
		}
	}

	private static native void showPickpointForm() /*-{
		$wnd.IMAGEBOOK.pickPoint.showPickpointWizard();
	}-*/;

}
