package ru.imagebook.client.admin.view.user;

import ru.imagebook.shared.model.Address;

import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class AddressFieldSet extends FieldSet {
	private Address address;
	private TextField<String> lastNameField;
	private TextField<String> nameField;
	private TextField<String> surnameField;
	private TextField<String> phoneField;
	private TextField<String> countryField;
	private TextField<String> indexField;
	private TextField<String> regionField;
	private TextField<String> cityField;
	private TextField<String> streetField;
	private TextField<String> homeField;
	private TextField<String> buildingField;
	private TextField<String> officeField;
    private TextField<String> porchField;
    private TextField<String> floorField;
    private TextField<String> intercomField;
	private TextArea commentField;

	public AddressFieldSet(UserConstants constants) {
		setCollapsible(true);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(150);
		setLayout(layout);
		setHeading(constants.address());

		lastNameField = new TextField<String>();
		lastNameField.setFieldLabel(constants.lastName());
		add(lastNameField);

		nameField = new TextField<String>();
		nameField.setFieldLabel(constants.name());
		add(nameField);

		surnameField = new TextField<String>();
		surnameField.setFieldLabel(constants.surname());
		add(surnameField);

		phoneField = new TextField<String>();
		phoneField.setFieldLabel(constants.phone());
		add(phoneField);

		countryField = new TextField<String>();
		countryField.setFieldLabel(constants.country());
		add(countryField);

		regionField = new TextField<String>();
		regionField.setFieldLabel(constants.region());
		add(regionField);

		indexField = new TextField<String>();
		indexField.setFieldLabel(constants.index());
		add(indexField);

		cityField = new TextField<String>();
		cityField.setFieldLabel(constants.city());
		
		add(cityField);

		streetField = new TextField<String>();
		streetField.setFieldLabel(constants.street());
		add(streetField);

		homeField = new TextField<String>();
		homeField.setFieldLabel(constants.home());
		add(homeField);

		buildingField = new TextField<String>();
		buildingField.setFieldLabel(constants.building());
		add(buildingField);

		officeField = new TextField<String>();
		officeField.setFieldLabel(constants.office());
		add(officeField);

        porchField = new TextField<String>();
        porchField.setFieldLabel(constants.porch());
        add(porchField);

        floorField = new TextField<String>();
        floorField.setFieldLabel(constants.floor());
        add(floorField);

        intercomField = new TextField<String>();
        intercomField.setFieldLabel(constants.intercom());
        add(intercomField);

		LabelField labelField = new LabelField();
		labelField.setFieldLabel(constants.addressComment() + ":");
		add(labelField);

		commentField = new TextArea();
		commentField.setFieldLabel(constants.addressComment());
		commentField.setHideLabel(true);
		add(commentField, new FormData(400, 100));

		setValue(address);
	}

	public void setValue(Address address) {
		if (address == null)
			return;

		this.address = address;

		lastNameField.setValue(address.getLastName());
		nameField.setValue(address.getName());
		surnameField.setValue(address.getSurname());
		phoneField.setValue(address.getPhone());
		countryField.setValue(address.getCountry());
		regionField.setValue(address.getRegion());
		indexField.setValue(address.getIndex());
		cityField.setValue(address.getCity());
		streetField.setValue(address.getStreet());
		homeField.setValue(address.getHome());
		buildingField.setValue(address.getBuilding());
		officeField.setValue(address.getOffice());
        porchField.setValue(address.getPorch());
        floorField.setValue(address.getFloor());
        intercomField.setValue(address.getIntercom());
		commentField.setValue(address.getComment());
	}

	public Address fetch() {
		if (address == null)
			address = new Address();

		address.setLastName(lastNameField.getValue());
		address.setName(nameField.getValue());
		address.setSurname(surnameField.getValue());
		address.setPhone(phoneField.getValue());
		address.setCountry(countryField.getValue());
		address.setIndex(indexField.getValue());
		address.setRegion(regionField.getValue());
		address.setCity(cityField.getValue());
		address.setStreet(streetField.getValue());
		address.setHome(homeField.getValue());
		address.setBuilding(buildingField.getValue());
		address.setOffice(officeField.getValue());
        address.setPorch(porchField.getValue());
        address.setFloor(floorField.getValue());
        address.setIntercom(intercomField.getValue());
		address.setComment(commentField.getValue());
		return address;
	}
}
