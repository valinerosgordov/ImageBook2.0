package ru.imagebook.client.common.service;

import ru.imagebook.shared.model.Flyleaf;
import ru.imagebook.shared.model.Vellum;

public interface Calc {
	@Deprecated
    int getCostWithoutDiscount(int quantity);
    
	int getImagebookPrice();

	double getFlyleafPrice(Flyleaf flyleaf);

	double getVellumPrice(Vellum vellum);

	int getIBX(int quantity, int level);
	
	int getIBXOldPrice(int quantity, int level);

	int getPrintingHousePrice();
}
