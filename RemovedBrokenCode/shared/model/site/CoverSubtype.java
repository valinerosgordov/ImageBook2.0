package ru.imagebook.shared.model.site;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoverSubtype {
	private String name;
	private List<Integer> productTypes = new ArrayList<Integer>();

	public CoverSubtype(String name, Integer[] productTypes) {
		this.name = name;
		this.productTypes.addAll(Arrays.asList(productTypes));
	}

	public String getName() {
		return name;
	}

	public List<Integer> getProductTypes() {
		return productTypes;
	}
}
