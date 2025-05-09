package ru.minogin.core.server.hibernate.updater;

import ru.minogin.core.client.address.Address;

public class AddressUpdater {
	public static Address getUpdatedAddress(Address address, Address modified) {
		if (modified == null)
			return null;

		address.setCountry(modified.getCountry());
		address.setZip(modified.getZip());
		address.setRegion(modified.getRegion());
		address.setArea(modified.getArea());
		address.setCity(modified.getCity());
		address.setStreet(modified.getStreet());
		address.setHouse(modified.getHouse());
		address.setBuilding(modified.getBuilding());
		address.setApp(modified.getApp());
		address.setInfo(modified.getInfo());
		address.setRecipient(modified.getRecipient());
		return address;
	}
}
