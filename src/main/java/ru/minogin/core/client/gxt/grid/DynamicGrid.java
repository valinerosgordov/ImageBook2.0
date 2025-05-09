package ru.minogin.core.client.gxt.grid;

import java.util.List;

import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;

public class DynamicGrid extends Grid<ModelData> {
	private PagingToolBar pagingToolBar;
	private BasePagingLoader<PagingLoadResult<ModelData>> loader;

	public DynamicGrid(List<ColumnConfig> columns, DataProxy<PagingLoadResult<ModelData>> proxy) {
		loader = new BasePagingLoader<PagingLoadResult<ModelData>>(
				proxy);
		loader.setRemoteSort(true);

		this.store = new ListStore<ModelData>(loader);
		this.cm = new ColumnModel(columns);
		this.view = new GridView();
		this.view.setForceFit(true);
		focusable = true;
		disabledStyle = null;
		baseStyle = "x-grid-panel";
		setSelectionModel(new GridSelectionModel<ModelData>());

		pagingToolBar = new PagingToolBar(0);
		pagingToolBar.bind(loader);
	}

	@Override
	protected void onResize(int width, int height) {
		super.onResize(width, height);

		float headerHeight = 24;
		float rowHeight = 22;
		int nRows = (int) ((height - headerHeight) / rowHeight);

		if (nRows > 0) {
			pagingToolBar.setPageSize(nRows);
			loader.load(loader.getOffset(), nRows);
		}
	}

	public BasePagingLoader<PagingLoadResult<ModelData>> getLoader() {
		return loader;
	}

	public PagingToolBar getPagingToolBar() {
		return pagingToolBar;
	}
}
