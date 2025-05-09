package ru.minogin.core.client.gxt;

import java.util.ArrayList;
import java.util.List;

import ru.minogin.core.shared.model.BaseEntity;

import com.extjs.gxt.ui.client.widget.grid.Grid;

public class GxtUtil {
	public static <E extends BaseEntity, M extends BaseEntityModel<E>> List<E> getEntities(
			List<M> models) {
		List<E> entities = new ArrayList<E>();
		for (M model : models) {
			entities.add(model.getEntity());
		}
		return entities;
	}

	public static <E extends BaseEntity, M extends BaseEntityModel<E>> E getSelectedEntity(
			Grid<M> grid) {
		M model = grid.getSelectionModel().getSelectedItem();
		if (model == null)
			return null;
		return model.getEntity();
	}

	public static <E extends BaseEntity, M extends BaseEntityModel<E>> List<E> getSelectedEntities(
			Grid<M> grid) {
		List<M> models = grid.getSelectionModel().getSelectedItems();
		return getEntities(models);
	}
}
