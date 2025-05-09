package ru.imagebook.server.tools;

import java.util.ArrayList;
import java.util.List;

import ru.imagebook.shared.model.Country;

public class CountryUtility {
	public static final List<Country> PopDefaultUp(List<Country> countries) {
		List<Country> result = new ArrayList<Country>();
		
		for (int i = 0; i < 2; i++) {
			for (Country country : countries) {
				if (i == 0 && country.getIsDefault()) {
					result.add(country);
				} else if (i == 1 && !country.getIsDefault()) {
					result.add(country);
				}
			}
		}
		
		return result;
	}
}
