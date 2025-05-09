package ru.minogin.bill.client;

import ru.minogin.bill.shared.RoboInfo;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;

public class RoboForm {
	private final RoboInfo info;

	public RoboForm(RoboInfo info) {
		this.info = info;
	}

	public void submit() {
		FormElement form = Document.get().createFormElement();
		form.setAction(info.getUrl());
		form.setMethod("POST");

		form.appendChild(createHiddenInput("MrchLogin", info.getUsername()));
		form.appendChild(createHiddenInput("OutSum", info.getSum() + ""));
		form.appendChild(createHiddenInput("InvId", info.getOrderId() + ""));
		form.appendChild(createHiddenInput("Desc", info.getDesc()));
		form.appendChild(createHiddenInput("SignatureValue", info.getCrc()));
		form.appendChild(createHiddenInput("IncCurrLabel", ""));
		form.appendChild(createHiddenInput("Culture", "ru"));

		Document.get().getBody().appendChild(form);
		form.submit();
	}

	private InputElement createHiddenInput(String name, String value) {
		InputElement input = Document.get().createHiddenInputElement();
		input.setName(name);
		input.setValue(value);
		return input;
	}
}
