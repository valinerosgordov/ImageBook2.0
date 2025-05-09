package ru.minogin.core.client.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DoubleMap<K1, K2, V> {
	private Map<K1, Map<K2, V>> map = new HashMap<K1, Map<K2, V>>();

	public V put(K1 key1, K2 key2, V value) {
		Map<K2, V> subMap = map.get(key1);
		if (subMap == null) {
			subMap = new HashMap<K2, V>();
			map.put(key1, subMap);
		}
		return subMap.put(key2, value);
	}

	public V get(K1 key1, K2 key2) {
		Map<K2, V> subMap = map.get(key1);
		if (subMap == null)
			return null;

		return subMap.get(key2);
	}

	public Set<K1> keySet() {
		return map.keySet();
	}

	public Map<K2, V> get(Object key1) {
		return map.get(key1);
	}

	public Collection<K2> getValues(Object key1) {
		return map.get(key1).keySet();
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DoubleMap))
			return false;

		return ((DoubleMap) obj).map.equals(map);
	}
	
	@Override
	public int hashCode() {
		return map.hashCode();
	}
}
