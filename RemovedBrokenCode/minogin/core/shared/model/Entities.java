package ru.minogin.core.shared.model;

import java.util.ArrayList;
import java.util.List;

public class Entities {
	public static List<Integer> getIds(List<? extends BaseEntity> entities) {
		List<Integer> ids = new ArrayList<Integer>();
		for (BaseEntity entity : entities) {
			ids.add(entity.getId());
		}
		return ids;
	}
}
