package ru.minogin.core.client.gxt.grid;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class BooleanColumnConfig extends XColumnConfig {
	public BooleanColumnConfig(String id, String name, int width) {
		super(id, name, width);

		setRenderer(new GridCellRenderer<ModelData>() {
			@Override
			public Object render(ModelData model, String property, ColumnData config,
					int rowIndex, int colIndex, ListStore<ModelData> store,
					Grid<ModelData> grid) {
				boolean value = (Boolean) model.get(property);
				return value ? "v" : "";
			}
		});
		setAlignment(HorizontalAlignment.CENTER);
	}
}
