package ru.minogin.core.client.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import ru.minogin.core.client.i18n.MultiString;
import ru.minogin.core.client.model.IdMap;
import ru.minogin.core.client.model.Identifiable;

public class PersistenceSupport {
	public static void copy(Bean target, Bean prototype) {
		for (String name : prototype.getPropertyNames()) {
			Object value = prototype.get(name);
			target.set(name, copy(value));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Object copy(Object value) {
		if (value == null)
			return null;
		else if (value instanceof String || value instanceof Integer || value instanceof Long
				|| value instanceof Double || value instanceof Boolean || value instanceof Date
				|| value instanceof MultiString)
			return value;
		else if (value instanceof ArrayList) {
			ArrayList list = (ArrayList) value;
			ArrayList copy = new ArrayList();
			for (Object element : list) {
				copy.add(copy(element));
			}
			return copy;
		}
		else if (value instanceof LinkedHashMap) {
			LinkedHashMap map = (LinkedHashMap) value;
			LinkedHashMap copy = new LinkedHashMap();
			for (Object key : map.keySet()) {
				copy.put(key, copy(map.get(key)));
			}
			return copy;
		}
		else if (value instanceof HashMap) {
			HashMap map = (HashMap) value;
			HashMap copy = new HashMap();
			for (Object key : map.keySet()) {
				copy.put(key, copy(map.get(key)));
			}
			return copy;
		}
		else if (value instanceof LinkedHashSet) {
			LinkedHashSet set = (LinkedHashSet) value;
			LinkedHashSet copy = new LinkedHashSet();
			for (Object element : set) {
				copy.add(copy(element));
			}
			return copy;
		}
		else if (value instanceof HashSet) {
			HashSet set = (HashSet) value;
			HashSet copy = new HashSet();
			for (Object element : set) {
				copy.add(copy(element));
			}
			return copy;
		}
		else if (value instanceof IdMap) {
			IdMap list = (IdMap) value;
			IdMap copy = new IdMap();
			for (Object element : list) {
				copy.add((Identifiable) copy(element));
			}
			return copy;
		}
		else if (value instanceof BasePersistentBean) {
			BasePersistentBean bean = (BasePersistentBean) value;
			return bean.copy();
		}
		else if (value instanceof BasePersistentIdBean) {
			BasePersistentIdBean bean = (BasePersistentIdBean) value;
			return bean.copy();
		}
		else
			throw new RuntimeException("Prototyping not supported for type: " + value.getClass());
	}

	public static void loadFrom(Bean target, Map<String, Object> image) {
		for (String name : image.keySet()) {
			target.set(name, image.get(name));
		}
	}

	public static void saveTo(Bean target, Map<String, Object> image) {
		for (String name : target.getPropertyNames()) {
			image.put(name, target.get(name));
		}
	}
}
