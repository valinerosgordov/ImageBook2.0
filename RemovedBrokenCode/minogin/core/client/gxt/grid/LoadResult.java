package ru.minogin.core.client.gxt.grid;

import java.util.List;

import ru.minogin.core.client.rpc.Transportable;

public class LoadResult<T> implements Transportable {
	private static final long serialVersionUID = 8006014779957949253L;

	private List<T> objects;
	private int offset;
	private long total;

	LoadResult() {}

	public LoadResult(List<T> objects, int offset, long total) {
		this.objects = objects;
		this.offset = offset;
		this.total = total;
	}

	public List<T> getObjects() {
		return objects;
	}

	public int getOffset() {
		return offset;
	}

	public long getTotal() {
		return total;
	}
}
