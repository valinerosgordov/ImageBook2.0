package ru.imagebook.client.editor.view.file;

import java.util.List;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.widget.treepanel.TreePanel;

public class XTreePanel<M extends ModelData> extends TreePanel<M> {
	public XTreePanel(TreeStore<M> store) {
		super(store);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void stExpand(List<M> children) {
		if (isStateful() && store.getKeyProvider() != null) {
			List<String> expanded = (List) getState().get("expanded");
			if (expanded != null) {
				for (M child : children) {
					String id = store.getKeyProvider().getKey(child);
					if (expanded.contains(id)) {
						setExpanded(child, true);
					}
					stExpand(store.getChildren(child));
				}
			}
		}
	}
}
