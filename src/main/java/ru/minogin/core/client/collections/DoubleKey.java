package ru.minogin.core.client.collections;

public class DoubleKey<A, B> {
	private A key1;
	private B key2;
	
	public DoubleKey(A key1, B key2) {
		this.key1 = key1;
		this.key2 = key2;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DoubleKey))
			return false;
		
		DoubleKey<A, B> doubleKey = (DoubleKey<A, B>) obj;
		return doubleKey.key1.equals(key1) && doubleKey.key2.equals(key2);
	}
	
	@Override
	public int hashCode() {
		return key1.hashCode() ^ key2.hashCode();
	}
}
