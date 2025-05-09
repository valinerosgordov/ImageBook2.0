package ru.minogin.core.client.gxt.form;

import ru.minogin.core.client.address.Address;
import ru.minogin.core.client.gxt.GxtConstants;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;

public class AddressField extends FieldSet {
	private GxtConstants constants = GWT.create(GxtConstants.class);

	private final boolean showCountry;
	private TextField<String> countryField;
	private final TextField<String> zipField;
	private final TextField<String> regionField;
	private final TextField<String> areaField;
	private final TextField<String> cityField;
	private final TextField<String> streetField;
	private final TextField<String> houseField;
	private final TextField<String> buildingField;
	private final TextField<String> appField;
	private final TextArea infoField;
	private final TextField<String> recipientField;

	private Address address = new Address();

	public AddressField(String label, LayoutContainer container,
			boolean showCountry) {
		this.showCountry = showCountry;

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(XFormPanel.LABEL_WIDTH);
		setLayout(layout);
		setHeading(label);
		setCollapsible(true);
		setExpanded(false);

		if (showCountry)
			countryField = new XTextField(constants.countryField(), true, this);

		zipField = new XTextField(constants.zipField(), true, this);
		regionField = new XTextField(constants.regionField(), true, this);
		areaField = new XTextField(constants.areaField(), true, this);
		cityField = new XTextField(constants.cityField(), true, this);
		streetField = new XTextField(constants.streetField(), true, this);
		houseField = new XTextField(constants.houseField(), true, this);
		buildingField = new XTextField(constants.buildingField(), true, this);
		appField = new XTextField(constants.appField(), true, this);
		recipientField = new XTextField(constants.recipientField(), true, this);
		infoField = new XTextArea(constants.infoField(), true, this);

		container.add(this);
	}

	public AddressField(String label, LayoutContainer container) {
		this(label, container, true);
	}

	public void setValue(Address address) {
		if (address == null)
			address = new Address();

		this.address = address;

		if (showCountry)
			countryField.setValue(address.getCountry());

		zipField.setValue(address.getZip());
		regionField.setValue(address.getRegion());
		areaField.setValue(address.getArea());
		cityField.setValue(address.getCity());
		streetField.setValue(address.getStreet());
		houseField.setValue(address.getHouse());
		buildingField.setValue(address.getBuilding());
		appField.setValue(address.getApp());
		recipientField.setValue(address.getRecipient());
		infoField.setValue(address.getInfo());
	}

	public Address getValue() {
		String country = null;
		if (showCountry)
			country = countryField.getValue();
		address.setCountry(country);

		address.setZip(zipField.getValue());
		address.setRegion(regionField.getValue());
		address.setArea(areaField.getValue());
		address.setCity(cityField.getValue());
		address.setStreet(streetField.getValue());
		address.setHouse(houseField.getValue());
		address.setBuilding(buildingField.getValue());
		address.setApp(appField.getValue());
		address.setRecipient(recipientField.getValue());
		address.setInfo(infoField.getValue());

		if (address.isEmpty())
			return null;

		return address;
	}
}
