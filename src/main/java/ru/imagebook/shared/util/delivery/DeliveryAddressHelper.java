package ru.imagebook.shared.util.delivery;

import ru.imagebook.shared.model.Address;
import ru.imagebook.shared.model.Bill;

public interface DeliveryAddressHelper {
    String getAddressString(Bill bill);

    Address getAddress(Bill bill);
}
